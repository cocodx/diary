import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author amazfit
 * @date 2022-08-13 上午2:22
 **/
public class TestUser {

    public static void main(String[] args) throws IOException {
        TestUser testUser = new TestUser();
        URL resource = testUser.getClass().getClassLoader().getResource("\\webapp\\userImagePath\\1.txt");

        System.out.println(System.getProperty("user.dir"));
        System.out.println(new File("").getAbsolutePath());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
    }
}
