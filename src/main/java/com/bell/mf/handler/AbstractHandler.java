package com.bell.mf.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bell.mf.IMessageFrame;
import com.bell.mf.enums.ParameterNameEnum;
import com.bell.mf.repository.HandlerRepository;

/**
 * AbstractHandler，指令码处理抽象类
 * @author bell.zhouxiaobing
 * @since 1.3
 */
public abstract class AbstractHandler implements Handler{
	
	protected abstract HandlerRepository getRepository();
	
	/**
	 * 使用反射来调用指令码对应的处理方法
	 * @param request
	 * @throws MessageFrameHandlerException
	 */
	protected void doHandle(MessageFrameRequest request) throws MessageFrameHandlerException{
		IMessageFrame iMessageFrame = request.getMessageFrame();
		Method method = getRepository().getHandlerMethod(iMessageFrame.getCommandCode());
		if (method == null) {
			throw new MessageFrameHandlerException(String.format("指令码 [%s] 解析方法未找到",
					iMessageFrame.getCommandCode()));
		}
		try {
			method.invoke(getRepository().getHandler(iMessageFrame.getCommandCode()),
					getMethodArgs(request, method));
		} catch (Exception e) {
			throw new MessageFrameHandlerException(String.format("执行%s方法出错", method.getName()), e);
		}
	}

	protected Object[] getMethodArgs(MessageFrameRequest request, Method method) {
		List<Object> list = new ArrayList<Object>();
		String[] handlerMethodParameterNames = getParameterNames(request);

		for (String parameterName : handlerMethodParameterNames) {
			if (ParameterNameEnum.DEVICE_ID.getName().equals(parameterName)) {
				list.add(request.getDeviceId());
			} else if (ParameterNameEnum.MESSAGE_FRAME.getName().equals(parameterName)) {
				list.add(request.getMessageFrame());
			} else if (ParameterNameEnum.MESSAGE.getName().equals(parameterName)) {
				list.add(request.getMessage());
			} else if (ParameterNameEnum.SYS_DATE.getName().equals(parameterName)) {
				list.add(request.getSystemDate());
			}
		}
		return list.toArray();
	}

	protected String[] getParameterNames(MessageFrameRequest request) {
		return getRepository().getHandlerMethodParameterNames(request.getMessageFrame().getCommandCode());
	}

}
