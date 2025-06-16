package net.royling.lovelysparklepieces.ModEvents.ClientEvent;

import java.awt.*;

public class ColorUtil {
    private static final long GLOBAL_TIMER = System.currentTimeMillis();
    // 基础彩虹效果
    public static int getRainbow() {
        return getRainbow(1.0f);
    }
    //基础彩虹效果，但是可以调节速度
    public static int getRainbow(float speed) {
        float hue = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed % 3000) / 3000f;
        return Color.HSBtoRGB(hue, 0.8f, 0.9f) & 0x00FFFFFF;
    }
    //高级彩虹效果
    public static int getRainbowWave(float speed, float saturation) {
        float hue = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed % 2000) / 2000f;
        float wave = (float) Math.sin(System.currentTimeMillis() * 0.002) * 0.1f + 0.9f;
        return Color.HSBtoRGB(hue, saturation, wave) & 0x00FFFFFF;
    }
    // 柔和彩虹效果 (低饱和度，高亮度)
    public static int getPastelRainbow(float speed) {
        float hue = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed % 4000L) / 4000f;
        return Color.HSBtoRGB(hue, 0.5f, 0.95f) & 0x00FFFFFF;
    }
    // 呼吸灯效果 (单一颜色亮度变化)
    public static int getPulsatingColor(int baseColorHex, float speed, float minBrightness, float maxBrightness) {
        float[] hsb = Color.RGBtoHSB((baseColorHex >> 16) & 0xFF, (baseColorHex >> 8) & 0xFF, baseColorHex & 0xFF, null);
        float brightnessRange = maxBrightness - minBrightness;
        float brightnessOffset = (float) (Math.sin((System.currentTimeMillis() - GLOBAL_TIMER) * 0.001 * speed) + 1.0) / 2.0f;
        float currentBrightness = minBrightness + brightnessRange * brightnessOffset;
        return Color.HSBtoRGB(hsb[0], hsb[1], currentBrightness) & 0x00FFFFFF;
    }
    // 两种颜色之间平滑过渡
    public static int getShiftingColor(int color1Hex, int color2Hex, float speed) {
        float[] hsb1 = Color.RGBtoHSB((color1Hex >> 16) & 0xFF, (color1Hex >> 8) & 0xFF, color1Hex & 0xFF, null);
        float[] hsb2 = Color.RGBtoHSB((color2Hex >> 16) & 0xFF, (color2Hex >> 8) & 0xFF, color2Hex & 0xFF, null);
        float factor = (float) (Math.sin((System.currentTimeMillis() - GLOBAL_TIMER) * 0.0005 * speed) + 1.0) / 2.0f;
        float h1 = hsb1[0];
        float h2 = hsb2[0];
        float s1 = hsb1[1];
        float s2 = hsb2[1];
        float b1 = hsb1[2];
        float b2 = hsb2[2];
        float hue;
        if (Math.abs(h1 - h2) > 0.5f) {
            if (h1 < h2) h1 += 1.0f;
            else h2 += 1.0f;
        }
        hue = (h1 + factor * (h2 - h1)) % 1.0f;
        if (hue < 0) {
            hue += 1.0f;
        }
        float saturation = s1 + factor * (s2 - s1);
        float brightness = b1 + factor * (b2 - b1);
        return Color.HSBtoRGB(hue, saturation, brightness) & 0x00FFFFFF;
    }
    // 火焰渐变效果 (红、橙、黄之间变化)
    public static int getFireGradient(float speed, float minBrightness, float maxBrightness) {
        float hueCycle = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed % 2000L) / 2000f;
        float hue = hueCycle * 0.166f;
        float brightnessRange = maxBrightness - minBrightness;
        float brightnessOffset = (float) (Math.sin((System.currentTimeMillis() - GLOBAL_TIMER) * 0.0015 * speed) + 1.0) / 2.0f;
        float currentBrightness = minBrightness + brightnessRange * brightnessOffset;
        return Color.HSBtoRGB(hue, 1.0f, currentBrightness) & 0x00FFFFFF;
    }
    // 海洋渐变效果 (蓝、青、绿之间变化)
    public static int getOceanGradient(float speed, float minBrightness, float maxBrightness) {
        float hueCycle = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed % 3000L) / 3000f;
        float hue = 0.45f + hueCycle * (0.7f - 0.45f);
        float brightnessRange = maxBrightness - minBrightness;
        float brightnessOffset = (float) (Math.cos((System.currentTimeMillis() - GLOBAL_TIMER) * 0.0012 * speed) + 1.0) / 2.0f;
        float currentBrightness = minBrightness + brightnessRange * brightnessOffset;
        return Color.HSBtoRGB(hue, 0.85f, currentBrightness) & 0x00FFFFFF;
    }
    // 霓虹闪烁效果 (快速颜色变化和亮度脉冲)
    public static int getTechnoGlow(float speed, float saturation) {
        float hue = ((System.currentTimeMillis() - GLOBAL_TIMER) * speed * 2.0f % 1000L) / 1000f;
        float brightnessPulse = (float) Math.abs(Math.sin((System.currentTimeMillis() - GLOBAL_TIMER) * 0.005 * speed)); // 0.0 to 1.0
        float brightness = 0.6f + brightnessPulse * 0.4f;
        return Color.HSBtoRGB(hue, saturation, brightness) & 0x00FFFFFF;
    }
    // 静态颜色，但亮度波动
    public static int getStaticColorWithBrightnessWave(int baseColorHex, float speed, float minBrightness, float maxBrightness) {
        float[] hsb = Color.RGBtoHSB((baseColorHex >> 16) & 0xFF, (baseColorHex >> 8) & 0xFF, baseColorHex & 0xFF, null);
        float brightnessRange = maxBrightness - minBrightness;
        double timeFactor = (System.currentTimeMillis() - GLOBAL_TIMER) * 0.001 * speed;
        float wave = (float) (Math.sin(timeFactor) + Math.sin(timeFactor * 0.5 + 0.3) * 0.5);
        float normalizedWave = (wave / 1.5f + 1.0f) / 2.0f;
        float currentBrightness = minBrightness + brightnessRange * normalizedWave;
        currentBrightness = Math.max(minBrightness, Math.min(maxBrightness, currentBrightness));
        return Color.HSBtoRGB(hsb[0], hsb[1], currentBrightness) & 0x00FFFFFF;
    }
}
