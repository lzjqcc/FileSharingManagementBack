package com.Lowser.tool.interceptors;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.controller.ToolController;
import com.Lowser.tool.dao.domain.Limit;
import com.Lowser.tool.dao.domain.UploadLimit;
import com.Lowser.tool.dao.repository.LimitRepository;
import com.Lowser.tool.dao.repository.UploadLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
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
        if (handler instanceof  HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getBeanType().getName().equals(ToolController.class.getName())) {
                deleteYesterdayUploadLimit();
                System.out.println(request.getRequestURL());
                String action = request.getParameter("action");
                Limit limit = limitRepository.findByTypeAndAction(method.getMethod().getName(), action);
                if (limit != null && limit.getLimit()) {
                    UploadLimit uploadLimit = uploadLimitRepository.findByLimitIdAndIp(limit.getId(), request.getRemoteHost());
                    if (uploadLimit != null && uploadLimit.getUploadTimes() >= limit.getLimitTimes()) {
                        throw new BizException("今天超过最大上传次数");
                    }
                    if (uploadLimit == null) {
                        uploadLimit = new UploadLimit();
                        uploadLimit.setIp(request.getRemoteHost());
                        uploadLimit.setUploadTimes(1);
                        uploadLimit.setLimitId(limit.getId());
                    }else {
                        uploadLimit.setUploadTimes(uploadLimit.getUploadTimes() + 1);
                    }
                    uploadLimitRepository.save(uploadLimit);

                }
            }
        }
        return true;
    }
    private void deleteYesterdayUploadLimit() {
        uploadLimitRepository.deleteUploadLimitByInsertTimeBefore(new Date());
    }
}
