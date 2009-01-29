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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.voxattendant.VoxAttendant;

public class SessionCollector
    implements Runnable
{

    public SessionCollector()
    {
        if(collectorThread == null)
        {
            collectorThread = new Thread(this);
            collectorThread.start();
        }
    }

    public void run()
    {
        do
            try
            {
                Thread.sleep(collectorSleepTimeMillis);
                checkInvalidSession();
            }
            catch(InterruptedException e)
            {
                return;
            }
        while(true);
    }

    public void checkInvalidSession()
    {
        Set keySet = SessionUtil.getStoredSessionIds();
        HashSet invalidSessionsSet = new HashSet();
        if(keySet != null)
        {
            Iterator sessionIdIterator = keySet.iterator();
            do
            {
                if(!sessionIdIterator.hasNext())
                    break;
                String sessionId = (String)sessionIdIterator.next();
                javax.servlet.http.HttpSession session = SessionUtil.getSession(sessionId);
                if(session != null && !SessionUtil.isSessionValid(session))
                    invalidSessionsSet.add(sessionId);
            } while(true);
            removeInvalidSessions(invalidSessionsSet);
        }
    }

    private void removeInvalidSessions(Set invalidSessionsSet)
    {
        if(invalidSessionsSet != null)
        {
            String sessionId;
            for(Iterator i = invalidSessionsSet.iterator(); i.hasNext(); SessionUtil.removeSession(sessionId))
                sessionId = (String)i.next();

        }
    }

    private static Thread collectorThread;
    private static long collectorSleepTimeMillis;

    static 
    {
        collectorSleepTimeMillis = Long.decode(VoxAttendant.getProps().getProperty("collectorSleepTimeMillis")).longValue();
    }
}
