package vn.techmaster.ecommecerapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.techmaster.ecommecerapp.repository.DiscountCampaignRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyTaskScheduler {
    // Phương thức này sẽ chạy vào lúc 0 giờ 0 phút của mỗi ngày
    @Scheduled(cron = "0 0 0 * * *")
    public void runAtStartOfNewDay() {
        log.info("Chạy vào lúc 0 giờ 0 phút của mỗi ngày");
    }
}
