/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is vox-mail.
 *
 * The Initial Developer of the Original Code is Voxeo Corporation.
 * Portions created by Voxeo are Copyright (C) 2000-2007.
 * All rights reserved.
 * 
 * Contributor(s):
 * ICOA Inc. <info@icoa.com> (http://icoa.com)
 */

package org.voxattendant.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CustomizableBean
{

    public CustomizableBean()
    {
        propertyMap = null;
    }

    public void setProperty(Object key, Object value)
    {
        if(propertyMap == null)
            propertyMap = new HashMap();
        propertyMap.put(key, value);
    }

    public Object getProperty(Object key)
    {
        if(propertyMap != null)
            return propertyMap.get(key);
        else
            return null;
    }

    public String get(Object key)
    {
        return getStringProperty(key);
    }

    public String getStringProperty(Object key)
    {
        try
        {
            String s = "".concat(String.valueOf(String.valueOf(propertyMap.get(key).toString())));
            return s;
        }
        catch(NullPointerException e)
        {
            String s1 = "";
            return s1;
        }
    }

    public Object removeProperty(Object key)
    {
        if(propertyMap != null)
            return propertyMap.remove(key);
        else
            return null;
    }

    public Set getPropertyKeys()
    {
        if(propertyMap != null)
            return propertyMap.keySet();
        else
            return null;
    }

    public Set getPropertyValues()
    {
        if(propertyMap != null)
            return new HashSet(propertyMap.values());
        else
            return null;
    }

    private HashMap propertyMap;
}
