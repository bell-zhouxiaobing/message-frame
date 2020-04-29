package com.bell.mf.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bell.mf.IMessageFrame;
import com.bell.mf.enums.ParameterNameEnum;
import com.bell.mf.repository.HandlerRepository;
import com.bell.mf.request.HandlerRequest;

/**
 * AbstractHandler，指令码处理抽象类
 * @author bell.zhouxiaobing
 * @since 1.3
 */
public abstract class AbstractHandler implements Handler {
	
	protected abstract HandlerRepository getHandlerRepository();

	protected abstract Object[] getMethodArgs(HandlerRequest request, Method method);
	
	/**
	 * 使用反射来调用指令码对应的处理方法
	 * @param request
	 * @throws HandlerException
	 */
	protected void doHandle(HandlerRequest request) throws HandlerException {
		IMessageFrame iMessageFrame = request.getMessageFrame();
		Method method = getHandlerRepository().getHandlerMethod(iMessageFrame.getCommandCode());
		if (method == null) {
			throw new HandlerException(String.format("指令码 [%s] 解析方法未找到",
					iMessageFrame.getCommandCode()));
		}
		try {
			method.invoke(getHandlerRepository().getHandler(iMessageFrame.getCommandCode()),
					getMethodArgs(request, method));
		} catch (Exception e) {
			throw new HandlerException(String.format("执行%s方法出错", method.getName()), e);
		}
	}

}
