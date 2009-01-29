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

package org.voxattendant.util;

import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil
{

    public SessionUtil()
    {
    }

    public static HttpSession getSession(HttpServletRequest req)
    {
        HttpSession session = req.getSession(false);
        if(session == null)
            session = req.getSession(true);
        else
        if(session != null && !isSessionValid(session))
        {
            removeSession(session.getId());
            session = req.getSession(true);
        }
        if(!sessionsMap.containsKey(session.getId()))
            sessionsMap.put(session.getId(), session);
        return session;
    }

    public static HttpSession getSession(String sessionId)
    {
        HttpSession session = (HttpSession)sessionsMap.get(sessionId);
        if(session == null)
            return null;
        if(session != null && !isSessionValid(session))
        {
            removeSession(session.getId());
            return null;
        } else
        {
            return session;
        }
    }

    public static Set getStoredSessionIds()
    {
        return sessionsMap.keySet();
    }

    public static void removeSession(String sessionId)
    {
        Object obj = new Object();
        synchronized(obj)
        {
            if(sessionId != null)
                sessionsMap.remove(sessionId);
        }
    }

    public static boolean isSessionValid(HttpSession session)
    {
        try
        {
            session.getAttribute("foo");
            boolean flag = true;
            return flag;
        }
        catch(IllegalStateException e)
        {
            boolean flag1 = false;
            return flag1;
        }
    }

    private static HashMap sessionsMap = new HashMap();

}
