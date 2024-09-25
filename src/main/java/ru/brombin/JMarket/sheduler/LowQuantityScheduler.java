package ru.brombin.JMarket.sheduler;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.service.ItemService;

import java.util.List;

import org.springframework.stereotype.Component;
import ru.brombin.JMarket.service.NotificationService;

@Component
@AllArgsConstructor
public class LowQuantityScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LowQuantityScheduler.class);
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyLowStockItems() {
        LOGGER.info("Запуск задания по проверке товаров с низким остатком...");

        int threshold = 10;
        List<Item> lowStockItems = itemService.findItemsWithLowQuantity(threshold);

        lowStockItems.forEach(item -> {
            notificationService.notifySeller(item.getOwner(), item);
            LOGGER.info("Продавец {} уведомлён о низком остатке товара с ID {}.",
                    item.getOwner().getUsername(), item.getId());
        });
    }
}

