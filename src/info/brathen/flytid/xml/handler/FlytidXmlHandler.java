/**
 * 
 */
package info.brathen.flytid.xml.handler;

import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Christoffer
 *
 */

public abstract class FlytidXmlHandler <T> extends DefaultHandler {
	
	/**
	 * This method is supposed to be overridden
	 * @return List of elements
	 */
	public abstract ArrayList<T> getElements() ;
}
