import com.mit.app.Application;

public class AdminTest {
	public static void main(String[] args) {
		Application.main(args);
		System.out.println(Application.EnvConfig.getProperty("spring.data.mongodb.host"));
	}
}
