package org.ade.monitoring.keberadaan.service.util;

import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;

public interface IBindMonakServiceConnection {
	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak);
	public void setBound(boolean bound);
}
