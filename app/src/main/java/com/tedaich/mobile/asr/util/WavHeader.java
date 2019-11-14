package com.tedaich.mobile.asr.util;

import androidx.annotation.NonNull;

public class WavHeader {
    private byte[] mHeader;          // the complete header.
    private int mSampleRate;         // sampling frequency in Hz (e.g. 44100).
    private int mChannels;           // number of channels.
    private int mNumSamples;         // total number of samples per channel.
    private int mNumBytesPerSample;  // number of bytes per sample, all channels included.

    public WavHeader(int sampleRate, int numChannels, int numBytesPerSample, int totalByteSize) {
        mSampleRate = sampleRate;
        mChannels = numChannels;
        mNumSamples = totalByteSize / (numChannels * numBytesPerSample);
        mNumBytesPerSample = numBytesPerSample * mChannels;
        mHeader = null;
        setHeader();
    }

    private void setHeader() {
        byte[] header = new byte[44];
        int offset = 0;
        int size;

        // set the RIFF chunk
        System.arraycopy(new byte[] {'R', 'I', 'F', 'F'}, 0, header, offset, 4);
        offset += 4;
        size = 36 + mNumSamples * mNumBytesPerSample;
        header[offset++] = (byte)(size & 0xFF);
        header[offset++] = (byte)((size >> 8) & 0xFF);
        header[offset++] = (byte)((size >> 16) & 0xFF);
        header[offset++] = (byte)((size >> 24) & 0xFF);
        System.arraycopy(new byte[] {'W', 'A', 'V', 'E'}, 0, header, offset, 4);
        offset += 4;

        // set the fmt chunk
        System.arraycopy(new byte[] {'f', 'm', 't', ' '}, 0, header, offset, 4);
        offset += 4;
        System.arraycopy(new byte[] {0x10, 0, 0, 0}, 0, header, offset, 4);  // chunk size = 16
        offset += 4;
        System.arraycopy(new byte[] {1, 0}, 0, header, offset, 2);  // format = 1 for PCM
        offset += 2;
        header[offset++] = (byte)(mChannels & 0xFF);
        header[offset++] = (byte)((mChannels >> 8) & 0xFF);
        header[offset++] = (byte)(mSampleRate & 0xFF);
        header[offset++] = (byte)((mSampleRate >> 8) & 0xFF);
        header[offset++] = (byte)((mSampleRate >> 16) & 0xFF);
        header[offset++] = (byte)((mSampleRate >> 24) & 0xFF);
        int byteRate = mSampleRate * mNumBytesPerSample;
        header[offset++] = (byte)(byteRate & 0xFF);
        header[offset++] = (byte)((byteRate >> 8) & 0xFF);
        header[offset++] = (byte)((byteRate >> 16) & 0xFF);
        header[offset++] = (byte)((byteRate >> 24) & 0xFF);
        header[offset++] = (byte)(mNumBytesPerSample & 0xFF);
        header[offset++] = (byte)((mNumBytesPerSample >> 8) & 0xFF);
        System.arraycopy(new byte[] {0x10, 0}, 0, header, offset, 2);
        offset += 2;

        // set the beginning of the data chunk
        System.arraycopy(new byte[] {'d', 'a', 't', 'a'}, 0, header, offset, 4);
        offset += 4;
        size = mNumSamples * mNumBytesPerSample;
        header[offset++] = (byte)(size & 0xFF);
        header[offset++] = (byte)((size >> 8) & 0xFF);
        header[offset++] = (byte)((size >> 16) & 0xFF);
        header[offset++] = (byte)((size >> 24) & 0xFF);

        mHeader = header;
    }

    public byte[] getWavHeader() {
        return mHeader;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";
        if (mHeader == null) {
            return str;
        }
        int num_32bits_per_lines = 8;
        int count = 0;
        for (byte b : mHeader) {
            boolean break_line = count > 0 && count % (num_32bits_per_lines * 4) == 0;
            boolean insert_space = count > 0 && count % 4 == 0 && !break_line;
            if (break_line) {
                str += '\n';
            }
            if (insert_space) {
                str += ' ';
            }
            str += String.format("%02X", b);
            count++;
        }
        return str;
    }

}
