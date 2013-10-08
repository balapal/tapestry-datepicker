package com.trsvax.datepicker.mixins;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.BindParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.trsvax.datepicker.DatePickerConstants;

@Import(library="classpath:/META-INF/assets/datefield/jquery-ui-1.10.3.custom.min.js"
//stylesheet="classpath:/META-INF/assets/datefield/jquery-ui-1.10.3.custom.css"
)
@MixinAfter
public class JQueryDatePIcker {
	
	@BindParameter
	private Object value;
		
    @Inject
	@Path("classpath:/META-INF/assets/datefield/calendar.png")
    private Asset icon;
    
    @Inject
    private AssetSource assetSource;
    
    @Inject
    @Symbol(DatePickerConstants.JQUERY_CSS)
    private String css;
    
    @Inject
    @Symbol(DatePickerConstants.JQUERY_LIBRARY)
    private String javascript;
    
    
	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement clientElement;
	
	@Inject
	private FormSupport formSupport;
	
	private Element element;
	
	@BeginRender
	void beginRender(MarkupWriter writer) {		
		element = writer.getElement();
		element.forceAttributes("type","date");	
	}
	
	@AfterRender
	void afterRender(MarkupWriter writer) {
		String id = clientElement.getClientId();
		String clientID = javaScriptSupport.allocateClientId(id);
		String formID = formSupport.getClientId();
		Date date = (Date) value;
		String formatedDate = "";
		if ( date != null ) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			formatedDate = format.format(date);
		}
		Element hidden =  element.elementBefore("input", 
				"value",formatedDate,"type","hidden","class","form-control","id",clientID);
		javaScriptSupport.require("datepicker/datepicker").with(new JSONObject("id", id, "clientID", clientID,"formID",formID));
		javaScriptSupport.importStylesheet(assetSource.getExpandedAsset(css));
		javaScriptSupport.importJavaScriptLibrary(assetSource.getExpandedAsset(javascript));
	}
	
}
