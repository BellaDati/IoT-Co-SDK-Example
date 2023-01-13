package com.belladati.iot.ext.example;

import com.belladati.iot.collector.receiver.endpoint.audioml.AudioProcessor;
import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CustomAudioMLProcessor implements AudioProcessor {

    /**
     * This is copy of the default FFT implementation
     */
    @Override
    public AvgMax processStreamData(double[] rawData, int fftPoints) {
        int OF = 1;    //OF = overlap factor
        int windowStep = fftPoints/OF;
        int nX = (rawData.length - fftPoints)/windowStep;

        Double[] sums = null;
        Double[] maxs = null;
        for (int i = 0; i < nX; i++) {
            double[] fftData = Arrays.copyOfRange(rawData, i * windowStep, i * windowStep + fftPoints);
            DoubleFFT_1D transformer = new DoubleFFT_1D(fftPoints);
            transformer.realForward(fftData);

            List<Double> cleanedValues = new ArrayList<>();
            cleanedValues.add(0.0);
            int limit = fftPoints % 2 == 0 ? fftPoints - 2 : fftPoints - 1;
            IntStream.range(2, limit).filter(n -> n % 2 == 0)
                    .mapToDouble(n -> Math.sqrt((Math.pow(fftData[n], 2)) + Math.pow(fftData[n + 1], 2))).boxed()
                    .forEach(cleanedValues::add);
            if (fftPoints % 2 == 0) {
                Double lastFrequencyValue = Math.sqrt((Math.pow(fftData[fftPoints - 1], 2)) + Math.pow(fftData[1], 2));
                cleanedValues.add(lastFrequencyValue);
            } else {
                cleanedValues.add(fftData[1]);
            }
            if (sums == null) {
                sums = cleanedValues.toArray(new Double[0]);
                maxs = cleanedValues.toArray(new Double[0]);
            } else {
                for (int j = 0; j < cleanedValues.size(); j++) {
                    Double d = cleanedValues.get(j);
                    sums[j] += d;
                    if (d > maxs[j]) {
                        maxs[j] = d;
                    }
                }
            }
        }
        if (sums != null) {
            for (int j = 0; j < sums.length; j++) {
                sums[j] = sums[j] / nX;
            }
        }
        return new AvgMax(sums, maxs);
    }
}
