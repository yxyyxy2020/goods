package com.example.goods.controller;

import com.example.goods.service.InstorageService;
import com.example.goods.utils.*;
import com.example.goods.vo.Instorage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Administrator
 * @date 2020/6/12
 */
@RequestMapping("/in")
@RestController
public class InstorageController {

    @Resource
    private InstorageService instorageServiceImpl;

    /**
     * 获取入库信息
     * @param msg type，联系人 json对象
     * @return 入库信息
     */
    @PostMapping("/getInstorageInfo/{msg}")
    public Result getInstorageInfo(@PathVariable("msg") String msg) {
        HashMap map = JsonUtils.stringToObj(msg, HashMap.class);
        String typeStr = null;
        try {
            typeStr = (String) map.get("type");
        } catch (Exception e) {
            return Result.error("类型不能为空！");
        }

        Integer type = Integer.parseInt(typeStr);
        String linkman = (String) map.get("linkman");
        //直接返回
        return Result.ok().put("instorage", instorageServiceImpl.selInstorageInfo(type, linkman));
    }

    /**
     * 添加入库信息
     * @param instorage 入库信息对象
     * @return 是否成功
     */
    @PostMapping("/addInstorageInfo")
    public Result addInstorageInfo(@RequestBody com.example.goods.entity.Instorage instorage) {
        Assert.isBlank(instorage.getCompany(), "单位不能为空");
        Assert.isBlank(instorage.getDepartment(), "部门不能为空");
        Assert.isBlank(instorage.getPhone(), "手机号不能为空");
        Assert.isNull(instorage.getType(), "入库类型不能为空");
        Assert.isBlank(instorage.getLinkman(), "联系人不能为空");
        Assert.isNull(instorage.getGoodsids(), "物资不能为空");
        Assert.isNull(instorage.getAmount(), "物资数量不能为空");
        //生成编码
        instorage.setCode(CodeUtil.getInstorageCode());
        //生成时间
        instorage.setIntime(CalendarUtil.getDate());
        try {
            instorageServiceImpl.insInstorageInfo(instorage);
        } catch (Exception e) {
            return Result.error("发生未知错误！");
        }
        return Result.ok();
    }

    /**
     * 修改入库信息
     * @param instorage 待修改的入库信息对象
     * @return 是否成功
     */
    @PostMapping("/updInstorageInfo")
    public Result updInstorageInfo(@RequestBody com.example.goods.entity.Instorage instorage) {
        Assert.isNull(instorage.getId(), "id不能为空");
        Assert.isBlank(instorage.getCompany(), "单位不能为空");
        Assert.isBlank(instorage.getDepartment(), "部门不能为空");
        Assert.isBlank(instorage.getPhone(), "手机号不能为空");
        Assert.isBlank(instorage.getLinkman(), "联系人不能为空");
        Assert.isNull(instorage.getType(), "类型不能为空");
        Assert.isNull(instorage.getGoodsids(), "物资不能为空");
        Assert.isNull(instorage.getAmount(), "物资数量不能为空");

        try {
            instorageServiceImpl.updInstorageInfo(instorage);
        } catch (Exception e) {
            return Result.error("发生未知错误！");
        }
        return Result.ok();
    }

    /**
     * 删除入库信息
     * @param msg id
     * @return 是否成功
     */
    @PostMapping("/delInstorageInfo/{msg}")
    public Result delInstorageInfo(@PathVariable("msg") String msg) {
        Assert.isBlank(msg, "id不能为空");

        try {
            Integer id = Integer.parseInt(msg);
            instorageServiceImpl.delInstorageInfo(id);
        } catch (Exception e) {
            return Result.error("发生未知错误！");
        }
        return Result.ok();
    }
}