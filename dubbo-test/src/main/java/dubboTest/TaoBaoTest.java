package dubboTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taobao.stresstester.StressTestUtils;
import com.taobao.stresstester.core.StressTask;

public class TaoBaoTest {
	public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath:conf/spring.xml" });
//        final PromotionCartServiceClient demoService = (PromotionCartServiceClient) context.getBean("PromotionCartServiceClient"); // 获取远程服
        StressTestUtils.testAndPrint(100, 1000000, new StressTask() {
            public Object doTask() throws Exception {
                List<String> promoIds = new ArrayList<String>();
                promoIds.add("0722d1aa9dcb4881bb6c7ad7cae76df6");
//                ResultDTO<Boolean> deductionPriceResource = demoService.deductionPriceResource(promoIds, UUID.randomUUID().toString(), "1443", "951023562960");

                return null;
            }
        });
        System.exit(0);
    }
}
