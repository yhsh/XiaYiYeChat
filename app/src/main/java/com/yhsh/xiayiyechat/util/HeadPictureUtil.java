package com.yhsh.xiayiyechat.util;
/*
 * Copyright (c) 2020, smuyyh@gmail.com All Rights Reserved.
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 */

import java.util.Random;

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/2/5 12:34
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.util
 * 文件说明：生成随意头像的url
 */
public class HeadPictureUtil {
    private String[] url = {
            "http://image.biaobaiju.com/uploads/20180122/22/1516629810-pnBjHQktMi.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3353731109,2302364697&fm=26&gp=0.jpg",
            "http://image.biaobaiju.com/uploads/20180830/19/1535628133-ZYwhzQRoPx.jpg",
            "http://img1.imgtn.bdimg.com/it/u=4014407438,2354181309&fm=26&gp=0.jpg",
            "http://image.biaobaiju.com/uploads/20180122/12/1516595149-QzBkiCgFOx.jpg",
            "http://img0w.pconline.com.cn/pconline/1401/20/4196857_miao/spcgroup/width_640,qua_30/22434W4Q-10.jpg",
            "http://img.xker.com/xkerfiles/allimg/1307/221_130720212243_10.jpg",
            "http://uploads.xuexila.com/allimg/1704/2043164050-4.jpg",
            "http://img.qqzhi.com/uploads/2018-12-30/022531669.jpg",
            "http://e.hiphotos.baidu.com/zhidao/pic/item/728da9773912b31b9c6f240b8718367adab4e195.jpg",
            "http://gss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d52a2834349b033bb028ac1d10ce36d3d439bd51.jpg",
            "http://image.biaobaiju.com/uploads/20180919/21/1537363736-sWQuzhRbHY.jpg",
            "http://diy.qqjay.com/u/files/2013/0115/594cdb75bbac37ba139cc7f91a23e464.jpg",
            "https://ss0.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1929660420,382758384&fm=26&gp=0.jpg"};

    private HeadPictureUtil() {
    }

    private static HeadPictureUtil headPictureUtil = new HeadPictureUtil();

    public static HeadPictureUtil getInstance() {
        return headPictureUtil;
    }

    public String getSendHeadUrl() {
        Random random = new Random();
        int i = random.nextInt(url.length);
        return url[i];
    }

    public String getReceiveHeadUrl() {
        Random random = new Random();
        int i = random.nextInt(url.length);
        return url[i];
    }
}
