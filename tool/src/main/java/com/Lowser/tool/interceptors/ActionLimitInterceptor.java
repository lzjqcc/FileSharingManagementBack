package com.Lowser.tool.interceptors;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.dao.domain.Limit;
import com.Lowser.tool.dao.domain.UploadLimit;
import com.Lowser.tool.dao.repository.LimitRepository;
import com.Lowser.tool.dao.repository.UploadLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ActionLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private LimitRepository limitRepository;
    @Autowired
    private UploadLimitRepository uploadLimitRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ;
        if (request.getRequestURL().toString().contains("tool")) {
            deleteYesterdayUploadLimit();
            String type = request.getParameter("type");
            String action = request.getParameter("action");
            Limit limit = limitRepository.findByTypeAndAction(type, action);
            if (limit != null && limit.getLimit()) {
                UploadLimit uploadLimit = uploadLimitRepository.findByLimitIdAndIp(limit.getId(), request.getRemoteHost());
                if (uploadLimit != null && uploadLimit.getUploadTimes() >= limit.getLimitTimes()) {
                    throw new BizException("今天超过最大上传次数");
                }
                if (uploadLimit == null) {
                    uploadLimit.setIp(request.getRemoteHost().toString());
                    uploadLimit.setUploadTimes(1);
                }else {
                    uploadLimit.setUploadTimes(uploadLimit.getUploadTimes() + 1);
                }
                uploadLimitRepository.save(uploadLimit);

            }
        }
        return false;
    }
    private void deleteYesterdayUploadLimit() {
        uploadLimitRepository.deleteUploadLimitByInsertTimeBefore(new Date());
    }
}
