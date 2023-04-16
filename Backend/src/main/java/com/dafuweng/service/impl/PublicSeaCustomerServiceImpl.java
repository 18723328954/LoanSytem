package com.dafuweng.service.impl;

import com.dafuweng.entity.PublicSeaCustomer;
import com.dafuweng.dao.PublicSeaCustomerMapper;
import com.dafuweng.service.PublicSeaCustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 公海客户表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class PublicSeaCustomerServiceImpl extends ServiceImpl<PublicSeaCustomerMapper, PublicSeaCustomer> implements PublicSeaCustomerService {

}
