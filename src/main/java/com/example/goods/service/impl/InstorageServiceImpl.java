package com.example.goods.service.impl;

import com.example.goods.entity.Instorage;
import com.example.goods.mapper.InstorageMapper;
import com.example.goods.service.InstorageService;
import com.example.goods.utils.ListTransform;
import com.example.goods.utils.RRException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * @date 2020/6/14
 */
@Service
public class InstorageServiceImpl implements InstorageService {

    @Resource
    private InstorageMapper instorageMapper;

    @Override
    public List<com.example.goods.vo.Instorage> selInstorageInfo(Integer type, String linkman) {
        //先查询出入库信息
        List<Instorage> instorage = instorageMapper.selInstorageInfo(type, linkman);
        if (instorage == null || instorage.size() == 0) {
            return null;
        }
        //全部发放信息
        List<com.example.goods.vo.Instorage> instorages = new ArrayList<>();
        //遍历入库信息
        for (Instorage in : instorage) {
            //转换成VO类
            com.example.goods.vo.Instorage instorage1 = new com.example.goods.vo.Instorage(in);
            //查询出入库信息附带的库存资料
            List<HashMap<String, Object>> list = instorageMapper.selGoodsInfos(instorage1.getGoodsidsList());

            List<Integer> amountList = instorage1.getAmountList();
            //把库存资料和库存数量匹配
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("amount", amountList.get(i));
            }

            instorage1.setGoodsList(list);
            instorages.add(instorage1);
        }
        return instorages;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer insInstorageInfo(Instorage in) {
        instorageMapper.insInstorageInfo(in);

        String goodsids = in.getGoodsids();
        String amount = in.getAmount();
        List<Integer> goodsidsList = ListTransform.stringToList(goodsids);
        List<Integer> amountList = ListTransform.stringToList(amount);
        if (goodsidsList.size() != amountList.size()) {
            throw new RRException("物资数量和物资种类不一致");
        }
        for (int i = 0; i < goodsidsList.size(); i++){
            instorageMapper.updAmount(goodsidsList.get(i), amountList.get(i));
        }
        return null;
    }

    @Override
    public Integer updInstorageInfo(Instorage in) {
        return instorageMapper.updInstorageInfo(in);
    }

    @Override
    public Integer delInstorageInfo(Integer id) {
        return instorageMapper.delInstorageInfo(id);
    }
}