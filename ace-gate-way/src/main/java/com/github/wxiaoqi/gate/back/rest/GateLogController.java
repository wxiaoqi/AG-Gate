package com.github.wxiaoqi.gate.back.rest;

import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.gate.back.biz.GateLogBiz;
import com.github.wxiaoqi.gate.back.entity.GateLog;
import com.github.wxiaoqi.gate.back.entity.User;
import com.github.wxiaoqi.gate.common.msg.TableResultResponse;
import com.github.wxiaoqi.gate.common.rest.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-07-01 20:32
 */
@Controller
@RequestMapping("gateLog")
public class GateLogController extends BaseController<GateLogBiz,GateLog> {
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<GateLog> page(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "1")int offset, String name){
        Example example = new Example(GateLog.class);
        if(StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("menu", "%" + name + "%");
        }
        int count = baseBiz.selectCountByExample(example);
        PageHelper.startPage(offset, limit);
        return new TableResultResponse<GateLog>(count,baseBiz.selectByExample(example));
    }
}
