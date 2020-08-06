package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.item.feign.GmallPmsClient;
import com.atguigu.gmall.item.feign.GmallSmsClient;
import com.atguigu.gmall.item.feign.GmallWmsClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.vo.ItemVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Lee_engineer
 * @create 2020-08-03 19:57
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallWmsClient wmsClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public ItemVo loadData(Long skuId) {

        ItemVo itemVo = new ItemVo();

        CompletableFuture<SkuEntity> skuEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 根据skuId查询sku的信息1
            ResponseVo<SkuEntity> skuEntityResponseVo = this.pmsClient.querySkuById(skuId);
            SkuEntity skuEntity = skuEntityResponseVo.getData();
            if (skuEntity == null) {
                return null;
            }
            itemVo.setSkuId(skuId);
            itemVo.setTitle(skuEntity.getTitle());
            itemVo.setSubTitle(skuEntity.getSubtitle());
            itemVo.setPrice(skuEntity.getPrice());
            itemVo.setWeight(skuEntity.getWeight());
            itemVo.setDefaultImage(skuEntity.getDefaultImage());
            return skuEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> categoryCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据cid3查询分类信息2
            ResponseVo<List<CategoryEntity>> categoryResponseVo = this.pmsClient.queryAllCategoriesByCid3(skuEntity.getCatagoryId());
            List<CategoryEntity> categoryEntities = categoryResponseVo.getData();
            itemVo.setCategories(categoryEntities);
        }, threadPoolExecutor);

        CompletableFuture<Void> brandCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据品牌的id查询品牌3
            ResponseVo<BrandEntity> brandEntityResponseVo = this.pmsClient.queryBrandById(skuEntity.getBrandId());
            BrandEntity brandEntity = brandEntityResponseVo.getData();
            if (brandEntity != null) {
                itemVo.setBrandId(brandEntity.getId());
                itemVo.setBrandName(brandEntity.getName());
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> spuCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据spuId查询spu4
            ResponseVo<SpuEntity> spuEntityResponseVo = this.pmsClient.querySpuById(skuEntity.getSpuId());
            SpuEntity spuEntity = spuEntityResponseVo.getData();
            if (spuEntity != null) {
                itemVo.setSpuId(spuEntity.getId());
                itemVo.setSpuName(spuEntity.getName());
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> imageCompletableFuture = CompletableFuture.runAsync(() -> {
            // 跟据skuId查询图片5
            ResponseVo<List<SkuImagesEntity>> skuImagesResponseVo = this.pmsClient.getSkuImagesBySkuId(skuId);
            List<SkuImagesEntity> skuImagesEntities = skuImagesResponseVo.getData();
            itemVo.setImages(skuImagesEntities);
        }, threadPoolExecutor);

        CompletableFuture<Void> salesCompletableFuture = CompletableFuture.runAsync(() -> {
            // 根据skuId查询sku营销信息6
            ResponseVo<List<ItemSaleVo>> salesResponseVo = this.smsClient.getItemSaleInfoBySkuId(skuId);
            List<ItemSaleVo> sales = salesResponseVo.getData();
            itemVo.setSales(sales);
        }, threadPoolExecutor);

        CompletableFuture<Void> wareCompletableFuture = CompletableFuture.runAsync(() -> {
            // 根据skuId查询sku的库存信息7
            ResponseVo<List<WareSkuEntity>> wareSkuResponseVo = this.wmsClient.getWareSkuBySkuId(skuId);
            List<WareSkuEntity> wareSkuEntities = wareSkuResponseVo.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                itemVo.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrValueCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据spuId查询spu下的所有sku的销售属性8
            ResponseVo<List<SaleAttrValueVo>> saleAttrValueVoResponseVo = this.pmsClient.queryAllSaleAttrValueBySpuId(skuEntity.getSpuId());
            List<SaleAttrValueVo> saleAttrValueVos = saleAttrValueVoResponseVo.getData();
            itemVo.setSaleAttrs(saleAttrValueVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrCompletableFuture = CompletableFuture.runAsync(() -> {
            // 当前sku的销售属性9
            ResponseVo<List<SkuAttrValueEntity>> saleAttrResponseVo = this.pmsClient.querySearchSkuAttrValueBySkuId(skuId);
            List<SkuAttrValueEntity> skuAttrValueEntities = saleAttrResponseVo.getData();
            Map<Long, String> map = skuAttrValueEntities.stream().collect(Collectors.toMap(SkuAttrValueEntity::getAttrId, SkuAttrValueEntity::getAttrValue));
            itemVo.setSaleAttr(map);
        }, threadPoolExecutor);

        CompletableFuture<Void> skusJsonCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据spuId查询spu下的所有sku及销售属性的映射关系10
            ResponseVo<String> skusJsonResponseVo = this.pmsClient.querySkusJsonBySpuId(skuEntity.getSpuId());
            String skusJson = skusJsonResponseVo.getData();
            itemVo.setSkusJson(skusJson);
        }, threadPoolExecutor);

        CompletableFuture<Void> spuImageCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据spuId查询spu的海报信息11
            ResponseVo<SpuDescEntity> spuDescEntityResponseVo = this.pmsClient.querySpuDescById(skuEntity.getSpuId());
            SpuDescEntity spuDescEntity = spuDescEntityResponseVo.getData();
            if (spuDescEntity != null && StringUtils.isNotBlank(spuDescEntity.getDecript())) {
                String[] images = StringUtils.split(spuDescEntity.getDecript(), ",");
                itemVo.setSpuImages(Arrays.asList(images));
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> groupCompletableFuture = skuEntityCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据cid3 spuId skuId查询组及组下的规格参数及值 12
            ResponseVo<List<ItemGroupVo>> groupResponseVo = this.pmsClient.queryGroupsBySpuIdAndCid(skuEntity.getSpuId(), skuId, skuEntity.getCatagoryId());
            List<ItemGroupVo> itemGroupVos = groupResponseVo.getData();
            itemVo.setGroups(itemGroupVos);
        }, threadPoolExecutor);

        CompletableFuture.allOf(categoryCompletableFuture,brandCompletableFuture,spuCompletableFuture,imageCompletableFuture,
                salesCompletableFuture,wareCompletableFuture,saleAttrValueCompletableFuture,saleAttrCompletableFuture,
                skusJsonCompletableFuture,spuImageCompletableFuture,groupCompletableFuture).join();

        return itemVo;

    }


}
