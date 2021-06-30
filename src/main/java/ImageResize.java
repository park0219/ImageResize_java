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
        BufferedImage beforeImage = ImageIO.read(beforeFile);

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
        BufferedImage beforeImage = ImageIO.read(beforeFile);
        int width = (int) (beforeImage.getWidth() * percent / 100);
        int height = (int) (beforeImage.getHeight() * percent / 100);
        resize(beforeResizeImagePath, afterResizeImagePath, width, height);
    }

    public static void main(String[] args) throws IOException {

        String currentPath = new File(".").getCanonicalPath();
        String beforePath = currentPath.concat("/beforeImage.jpg");
        String afterPath = currentPath.concat("/afterImage.jpg");

        try {
            resizeWithPercent(beforePath, afterPath, 50);
            System.out.println("success");
        }
        catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
        }
    }
}
