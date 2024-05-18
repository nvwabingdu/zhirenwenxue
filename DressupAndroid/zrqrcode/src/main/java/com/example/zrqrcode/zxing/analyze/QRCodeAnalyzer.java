package com.example.zrqrcode.zxing.analyze;

import androidx.annotation.Nullable;

import com.example.zrqrcode.zxing.DecodeConfig;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Reader;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Map;


/**
 * 二维码分析器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class QRCodeAnalyzer extends BarcodeFormatAnalyzer {

    public QRCodeAnalyzer() {
        this((DecodeConfig) null);
    }

    public QRCodeAnalyzer(@Nullable Map<DecodeHintType, Object> hints) {
        this(new DecodeConfig().setHints(hints));
    }

    public QRCodeAnalyzer(@Nullable DecodeConfig config) {
        super(config);
    }

    @Override
    public Reader createReader() {
        return new QRCodeReader();
    }

}
