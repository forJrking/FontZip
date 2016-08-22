package util;

import java.io.IOException;

import com.google.typography.font.tools.sfnttool.SfntTool;

/**
 * 
 * 字体提取工具类
 * 
 * @author Melon
 * 
 */
public class FontGenerateManager {

    /**
     * 
     * 根据舒服的内容生成字体文件
     * 
     * @param content
     * @param sourceFontFile
     * @param generateFontFile
     * @return
     */
    public static void generateFontByContent(String content, String sourceFontFile, String generateFontFile) {

        String[] args = new String[] { "-s", content, sourceFontFile, generateFontFile };

        try {
            SfntTool.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}