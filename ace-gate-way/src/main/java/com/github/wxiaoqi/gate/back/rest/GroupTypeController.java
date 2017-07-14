package com.github.wxiaoqi.gate.back.rest;

import com.github.wxiaoqi.gate.back.biz.GroupTypeBiz;
import com.github.wxiaoqi.gate.back.entity.GroupType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.gate.back.biz.UserBiz;
import com.github.wxiaoqi.gate.back.entity.User;
import com.github.wxiaoqi.gate.common.msg.TableResultResponse;
import com.github.wxiaoqi.gate.common.rest.BaseController;

import tk.mybatis.mapper.entity.Example;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@Controller
@RequestMapping("groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz,GroupType> {

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<GroupType> page(int limit, int offset, String name){
        Example example = new Example(User.class);
        if(StringUtils.isNotBlank(name))
            example.createCriteria().andLike("name", "%" + name + "%");
        int count = baseBiz.selectCountByExample(example);
        PageHelper.startPage(offset, limit);
        return new TableResultResponse<GroupType>(count,baseBiz.selectByExample(example));
    }

}
