package ru.brombin.JMarket.sheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.service.ItemService;

import java.util.List;

import org.springframework.stereotype.Component;
import ru.brombin.JMarket.service.NotificationService;

@Component
@AllArgsConstructor
@Slf4j
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class LowQuantityScheduler {
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyLowQuantityItems() {
        log.info("Запуск задания по проверке товаров с низким остатком...");

        int threshold = 10;
        List<Item> lowQuantityItems = itemService.findItemsWithLowQuantity(threshold);

        lowQuantityItems.forEach(item -> {
            notificationService.notifySeller(item.getOwner(), item);
            log.info("Продавец {} уведомлён о низком остатке товара с ID {}.",
                    item.getOwner().getUsername(), item.getId());
        });
    }
}

