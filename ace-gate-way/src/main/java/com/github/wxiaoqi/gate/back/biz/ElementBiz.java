package com.github.wxiaoqi.gate.back.biz;

import com.github.wxiaoqi.gate.back.entity.Element;
import com.github.wxiaoqi.gate.back.mapper.ElementMapper;
import com.github.wxiaoqi.gate.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-23 20:27
 */
@Service
public class ElementBiz extends BaseBiz<ElementMapper,Element> {
    public List<Element> getAuthorityElementByUserId(String userId){
       return mapper.selectAuthorityElementByUserId(userId);
    }
    public List<Element> getAuthorityElementByUserId(String userId,String menuId){
        return mapper.selectAuthorityMenuElementByUserId(userId,menuId);
    }
}
