import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResize {

    /**
     * 입력된 너비, 높이로 이미지 리사이징
     * @param beforeResizeImagePath 원본 이미지 경로
     * @param afterResizeImagePath  리사이징한 이미지를 저장할 경로
     * @param width                 너비
     * @param height                높이
     */
    public static void resize(String beforeResizeImagePath, String afterResizeImagePath, int width, int height) throws IOException {
        //원본 파일로 입력 BufferedImage 생성
        File beforeFile = new File(beforeResizeImagePath);
        BufferedImage beforeImage = setBufferedImage(beforeFile);

        //출력 BufferedImage 생성
        BufferedImage afterImage = new BufferedImage(width, height, beforeImage.getType());

        //이미지 사이즈 변경
        Graphics2D g2d = afterImage.createGraphics();
        g2d.drawImage(beforeImage, 0, 0, width, height, null);
        g2d.dispose();

        //출력용 파일 확장명 구하기
        String format = afterResizeImagePath.substring(afterResizeImagePath.lastIndexOf(".") + 1);

        //이미지 출력
        ImageIO.write(afterImage, format, new File(afterResizeImagePath));
    }

    /**
     * 파일을 읽어와 너비, 높이를 구하고 입력된 %로 리사이징
     * @param beforeResizeImagePath 원본 이미지 경로
     * @param afterResizeImagePath  리사이징한 이미지를 저장할 경로
     * @param percent               퍼센트(10, 20, 30 ...)
     */
    public static void resizeWithPercent(String beforeResizeImagePath, String afterResizeImagePath, double percent) throws IOException {
        //원본 파일의 너비, 높이 가져오기
        File beforeFile = new File(beforeResizeImagePath);
        BufferedImage beforeImage = setBufferedImage(beforeFile);
        int width = (int) (beforeImage.getWidth() * percent / 100);
        int height = (int) (beforeImage.getHeight() * percent / 100);
        resize(beforeResizeImagePath, afterResizeImagePath, width, height);
    }

    public static BufferedImage setBufferedImage(File beforeFile) throws IOException {

        BufferedImage beforeImage = ImageIO.read(beforeFile);
        int originWidth = beforeImage.getWidth();
        int originHeight = beforeImage.getHeight();
        int orientation = 1; // 회전정보(1: 0도, 3: 180도, 6: 270도, 8: 90도)

        Metadata metadata; // 이미지 메타 데이터 객체
        Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체

        try {
            metadata = ImageMetadataReader.readMetadata(beforeFile);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(directory != null){
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage tempBI;
        if(orientation == 8) {
            tempBI = new BufferedImage(originHeight, originWidth, beforeImage.getType());
            for (int i = 0; i < originWidth; i++) {
                for (int j = 0; j < originHeight; j++) {
                    tempBI.setRGB(j, originWidth - i - 1, beforeImage.getRGB(i, j));
                }
            }
            beforeImage = tempBI;
        }
        else if(orientation == 6) {
            tempBI = new BufferedImage(originHeight, originWidth, beforeImage.getType());
            for (int i = 0; i < originWidth; i++) {
                for (int j = 0; j < originHeight; j++) {
                    tempBI.setRGB(originHeight - j - 1, i, beforeImage.getRGB(i, j));
                }
            }
            beforeImage = tempBI;
        }
        else if(orientation == 3) {
            tempBI = new BufferedImage(originWidth, originHeight, beforeImage.getType());
            for (int i = 0; i < originWidth; i++) {
                for (int j = 0; j < originHeight; j++) {
                    tempBI.setRGB(originWidth - i - 1, originHeight - j - 1, beforeImage.getRGB(i, j));
                }
            }
            beforeImage = tempBI;
        }
        return beforeImage;
    }

    public static void main(String[] args) throws IOException {

        String currentPath = new File(".").getCanonicalPath();
        String beforePath = currentPath.concat("/beforeImage.jpg");
        String afterPath = currentPath.concat("/afterImage.jpg");

        String beforePath2 = currentPath.concat("/beforeImage2.jpg");
        String afterPath2 = currentPath.concat("/afterImage2.jpg");

        try {
            resizeWithPercent(beforePath, afterPath, 50);
            resizeWithPercent(beforePath2, afterPath2, 50);
            System.out.println("success");
        }
        catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
        }
    }
}
