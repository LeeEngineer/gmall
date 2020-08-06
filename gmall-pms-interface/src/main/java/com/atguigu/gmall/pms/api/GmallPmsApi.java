package com.atguigu.gmall.pms.api;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallPmsApi {

    @PostMapping("pms/spu/json")
    ResponseVo<List<SpuEntity>> querySpuByPageJson(@RequestBody PageParamVo paramVo);

    @GetMapping("pms/sku/spu/{spuId}")
    ResponseVo<List<SkuEntity>> querySkuBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("pms/brand/{id}")
    ResponseVo<BrandEntity> queryBrandById(@PathVariable("id") Long id);

    @GetMapping("pms/category/{id}")
    ResponseVo<CategoryEntity> queryCategoryById(@PathVariable("id") Long id);

    @GetMapping("pms/spuattrvalue/search/{spuId}")
    ResponseVo<List<SpuAttrValueEntity>> querySpuAttrValueBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("pms/skuattrvalue/search/{skuId}")
    ResponseVo<List<SkuAttrValueEntity>> querySearchSkuAttrValueBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("pms/spu/{id}")
    ResponseVo<SpuEntity> querySpuById(@PathVariable("id") Long id);

    @GetMapping("pms/category/parent/{parentId}")
    ResponseVo<List<CategoryEntity>> getCateGoryByPid(@PathVariable("parentId") Long pid);

    @GetMapping("pms/category/subs/{pid}")
    ResponseVo<List<CategoryEntity>> queryCatogoriesWithSub(@PathVariable Long pid);

    @GetMapping("pms/sku/{id}")
    ResponseVo<SkuEntity> querySkuById(@PathVariable("id") Long id);

    @GetMapping("pms/spudesc/{spuId}")
    ResponseVo<SpuDescEntity> querySpuDescById(@PathVariable("spuId") Long spuId);

    @GetMapping("pms/category/all/{cid3}")
    ResponseVo<List<CategoryEntity>> queryAllCategoriesByCid3(@PathVariable ("cid3") Long cid3);

    @GetMapping("pms/skuimages/sku/{skuId}")
    ResponseVo<List<SkuImagesEntity>> getSkuImagesBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("pms/skuattrvalue/spu/sku/{spuId}")
    ResponseVo<String> querySkusJsonBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("pms/attrgroup/withattrvalues/{spuId}/{skuId}/{cid}")
    ResponseVo<List<ItemGroupVo>> queryGroupsBySpuIdAndCid(@PathVariable("spuId") Long spuId,
                                                                  @PathVariable("skuId") Long skuId,
                                                                  @PathVariable("cid") Long cid);

    @GetMapping("pms/skuattrvalue/spu/{spuId}")
    ResponseVo<List<SaleAttrValueVo>> queryAllSaleAttrValueBySpuId(@PathVariable("spuId")Long spuId);
}
