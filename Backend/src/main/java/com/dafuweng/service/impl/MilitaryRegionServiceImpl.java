package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.MilitaryRegionMapper;
import com.dafuweng.entity.MilitaryRegion;
import com.dafuweng.service.MilitaryRegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 战区表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class MilitaryRegionServiceImpl extends ServiceImpl<MilitaryRegionMapper, MilitaryRegion> implements MilitaryRegionService {

    @Override
    public MilitaryRegion getMilitaryRegionByMilitaryRegionId(Long militaryRegionId) {
        QueryWrapper<MilitaryRegion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", militaryRegionId);

        return baseMapper.selectOne(queryWrapper);
    }
}
