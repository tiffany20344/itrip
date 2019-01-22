package cn.itrip.biz.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.pojo.ItripLabelDic;
import cn.itrip.beans.vo.ItripAreaDicVO;
import cn.itrip.beans.vo.ItripLabelDicVO;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import cn.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import cn.itrip.biz.service.areadic.ItripAreaDicService;
import cn.itrip.biz.service.hotel.ItripHotelService;
import cn.itrip.biz.service.labeldic.ItripLabelDicService;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping("/api/hotel")
public class HotelController {

        @Resource
        private ItripHotelService itripHotelService;

        @Resource
        private ItripLabelDicService itripLabelDicService;

        @Resource
        private ItripAreaDicService itripAreaDicService;

        /***
         * 根据酒店id查询酒店特色和介绍 -add by donghai
         *
         * @return
         * @throws Exception
         */
        @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>10210: 酒店id不能为空</p>" +
                "<p>10211: 系统异常,获取失败</p>")
        @RequestMapping(value = "/queryhoteldetails/{id}", produces = "application/json", method = RequestMethod.GET)
        @ResponseBody
        public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(
                @ApiParam(required = true, name = "id", value = "酒店ID")
                @PathVariable Long id) {
                List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
                try {
                        if (EmptyUtils.isNotEmpty(id)) {
                                itripSearchDetailsHotelVOList = itripHotelService.queryHotelDetails(id);
                                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
                        } else {
                                return DtoUtil.returnFail("酒店id不能为空", "10210");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        return DtoUtil.returnFail("系统异常,获取失败", "10211");
                }
        }

        @ApiOperation(value = "根据酒店id查询酒店特色、商圈、酒店名称", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>错误码：</p>" +
                "<p>100214 : 获取酒店视频文字描述失败 </p>" +
                "<p>100215 : 酒店id不能为空</p>")
        @RequestMapping(value = "/getvideodesc/{hotelId}", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public Dto<Object> getVideoDescByHotelId(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                                 @PathVariable String hotelId) {
                Dto<Object> dto = new Dto<Object>();
               /* logger.debug("getVideoDescByHotelId hotelId : " + hotelId);*/
                if (null != hotelId && !"".equals(hotelId)) {
                        HotelVideoDescVO hotelVideoDescVO = null;
                        try {
                                hotelVideoDescVO = itripHotelService.getVideoDescByHotelId(Long.valueOf(hotelId));
                                dto = DtoUtil.returnSuccess("获取酒店视频文字描述成功", hotelVideoDescVO);
                        } catch (Exception e) {
                                e.printStackTrace();
                                dto = DtoUtil.returnFail("获取酒店视频文字描述失败", "100214");
                        }

                } else {
                        dto = DtoUtil.returnFail("酒店id不能为空", "100215");
                }
                return dto;
        }

        @ApiOperation(value = "根据酒店id查询酒店设施", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "根据酒店id查询酒店设施" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>10206: 酒店id不能为空</p>" +
                "<p>10207: 系统异常,获取失败</p>")
        @RequestMapping(value = "/queryhotelfacilities/{id}", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                                 @PathVariable long id) {
                ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
                try {
                        if (EmptyUtils.isNotEmpty(id)) {
                                itripSearchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
                                return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
                        } else {
                                return DtoUtil.returnFail("酒店id不能为空", "10206");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        return DtoUtil.returnFail("系统异常,获取失败", "10207");
                }
        }


        @ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "根据酒店id查询酒店政策" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>10208: 酒店id不能为空</p>" +
                "<p>10209: 系统异常,获取失败</p>")
        @RequestMapping(value = "/queryhotelpolicy/{id}", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public Dto<ItripSearchFacilitiesHotelVO> queryHotelPolicy(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                                                      @PathVariable long id) {
                ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
                try {
                        if (EmptyUtils.isNotEmpty(id)) {
                                itripSearchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
                                return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
                        } else {
                                return DtoUtil.returnFail("酒店id不能为空", "10206");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        return DtoUtil.returnFail("系统异常,获取失败", "10207");
                }
        }

        /****
         * 查询热门城市的接口
         *
         * @param type
         * @return
         * @throws Exception
         */
        @ApiOperation(value = "查询热门城市", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "查询国内、国外的热门城市(1:国内 2:国外)" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>错误码：</p>" +
                "<p>10201 : hotelId不能为空 </p>" +
                "<p>10202 : 系统异常,获取失败</p>")
        @RequestMapping(value = "/queryhotcity/{type}", produces = "application/json", method = RequestMethod.GET)
        @ResponseBody
        public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
                List<ItripAreaDic> itripAreaDics = null;
                List<ItripAreaDicVO> itripAreaDicVOs = null;
                try {
                        if (EmptyUtils.isNotEmpty(type)) {
                                Map param = new HashMap();
                                param.put("isHot", 1);
                                param.put("isChina", type);
                                itripAreaDics = itripAreaDicService.getItripAreaDicListByMap(param);
                                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                                        itripAreaDicVOs = new ArrayList();
                                        for (ItripAreaDic dic : itripAreaDics) {
                                                ItripAreaDicVO vo = new ItripAreaDicVO();
                                                BeanUtils.copyProperties(dic, vo);
                                                itripAreaDicVOs.add(vo);
                                        }
                                }

                        } else {
                                DtoUtil.returnFail("type不能为空", "10201");
                        }
                } catch (Exception e) {
                        DtoUtil.returnFail("系统异常", "10202");
                        e.printStackTrace();
                }
                return DtoUtil.returnDataSuccess(itripAreaDicVOs);
        }

        /***
         * 查询酒店特色列表
         *
         * @return
         * @throws Exception
         */
        @ApiOperation(value = "查询酒店特色列表", httpMethod = "GET",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "获取酒店特色(用于查询页列表)" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>错误码: </p>" +
                "<p>10205: 系统异常,获取失败</p>")
        @RequestMapping(value = "/queryhotelfeature", produces = "application/json", method = RequestMethod.GET)
        @ResponseBody
        public Dto<ItripLabelDicVO> queryHotelFeature() {
                List<ItripLabelDic> itripLabelDics = null;
                List<ItripLabelDicVO> itripAreaDicVOs = null;
                try {
                        Map param = new HashMap();
                        param.put("parentId", 16);
                        itripLabelDics = itripLabelDicService.getItripLabelDicListByMap(param);
                        if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                                itripAreaDicVOs = new ArrayList();
                                for (ItripLabelDic dic : itripLabelDics) {
                                        ItripLabelDicVO vo = new ItripLabelDicVO();
                                        BeanUtils.copyProperties(dic, vo);
                                        itripAreaDicVOs.add(vo);
                                }
                        }

                } catch (Exception e) {
                        DtoUtil.returnFail("系统异常", "10205");
                        e.printStackTrace();
                }
                return DtoUtil.returnDataSuccess(itripAreaDicVOs);
        }
}

