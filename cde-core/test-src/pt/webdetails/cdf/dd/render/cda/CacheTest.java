/*!
* Copyright 2002 - 2015 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cdf.dd.render.cda;


import junit.framework.TestCase;
import net.sf.json.JSONObject;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class CacheTest extends TestCase {

  private JXPathContext context;
  private Cache cacheRenderer;
  private Element dataAccess;
  private Element cache;
  private Element key;
  private Document doc;

  @Before
  public void setUp() throws Exception {
    context = mock( JXPathContext.class );
    cacheRenderer = new Cache( context );

    dataAccess = mock( Element.class );
    cache = mock( Element.class );
    key = mock( Element.class );
    doc = mock( Document.class );
    cacheRenderer.setDefinition( JSONObject.fromObject( "{\"value\":\"true\"}" ) );

    doReturn( doc ).when( dataAccess ).getOwnerDocument();
    doReturn( cache ).when( doc ).createElement( "Cache" );
    doReturn( key ).when( doc ).createElement( "Key" );
  }

  @Test
  public void testRenderCacheTag() {
    doReturn( "500" ).when( context ).getValue( "properties/.[name='cacheDuration']/value" );
    doReturn( "[]" ).when( context ).getValue( "properties/.[name='cacheKeys']/value" );

    cacheRenderer.renderInto( dataAccess );

    verify( cache, times(1)).setAttribute( "enabled", "true" );
    verify( cache, times(1)).setAttribute( "duration", "500" );
    verify( dataAccess, times(1)).appendChild( cache );
  }

  @Test
  public void testRenderCacheTagWithKeys() {
    doReturn( "3600" ).when( context ).getValue( "properties/.[name='cacheDuration']/value" );
    doReturn( "[[\"Hello\",\"World\"]]" ).when( context ).getValue( "properties/.[name='cacheKeys']/value" );

    cacheRenderer.renderInto( dataAccess );

    verify( cache, times(1)).setAttribute( "enabled", "true" );
    verify( cache, times(1)).setAttribute( "duration", "3600" );
    verify( dataAccess, times(1)).appendChild( cache );

    verify( key, times(1)).setAttribute( "name", "Hello" );
    verify( key, times(1)).setAttribute( "value", "World" );
    verify( cache, times(1)).appendChild( key );
  }
}
