/*
 * Copyright 2020 www.mytijian.com All right reserved. This software is the
 * confidential and proprietary information of www.mytijian.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with www.mytijian.com.
 */
package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFundTargetDetails;
import com.Lowser.personalAsserts.dao.domain.IpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IpLogRepository extends JpaRepository<IpLog, Integer> {
}
