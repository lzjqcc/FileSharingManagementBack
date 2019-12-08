package com.Lowser.personalAsserts.controller.vo;

import java.util.List;

public class AccountVo {
    private TargetAccountVO targetAccountVO;
    private List<CurrentAccountVO> currentAccountVOS;

    public TargetAccountVO getTargetAccountVO() {
        return targetAccountVO;
    }

    public void setTargetAccountVO(TargetAccountVO targetAccountVO) {
        this.targetAccountVO = targetAccountVO;
    }

    public List<CurrentAccountVO> getCurrentAccountVOS() {
        return currentAccountVOS;
    }

    public void setCurrentAccountVOS(List<CurrentAccountVO> currentAccountVOS) {
        this.currentAccountVOS = currentAccountVOS;
    }
}
