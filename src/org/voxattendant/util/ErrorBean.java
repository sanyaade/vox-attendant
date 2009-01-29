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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

public class ErrorBean
{

    public ErrorBean()
    {
        errorMessages = new Vector();
        errorFieldsMap = new HashMap();
    }

    public void addError(FormError error)
    {
        if(error.getFormField() != null)
            errorFieldsMap.put(error.getFormField(), error);
        errorMessages.addElement(error);
    }

    public void addError(String errorMessage, int severity, boolean display, String displayColor)
    {
        FormError error = new FormError(errorMessage, severity, display, displayColor);
        errorMessages.addElement(error);
    }

    public void addError(String errorMessage, int severity)
    {
        FormError error = new FormError(errorMessage, severity);
        errorMessages.addElement(error);
    }

    public FormError getErrorAt(int offset)
    {
        return (FormError)errorMessages.elementAt(offset);
    }

    public FormError getErrorByName(String message)
    {
label0:
        {
            FormError tempForm = null;
            try
            {
                Enumeration e = errorMessages.elements();
                do
                {
                    if(!e.hasMoreElements())
                        break label0;
                    tempForm = (FormError)e.nextElement();
                } while(tempForm.getErrorMessage() == null || !tempForm.getErrorMessage().equals(message));
                FormError formerror = tempForm;
                return formerror;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FormError getErrorByField(String field)
    {
        return (FormError)errorFieldsMap.get(field);
    }

    public int size()
    {
        return errorMessages.size();
    }

    public int severity()
    {
        int severity = 0x7fffffff;
        int tempseverity = 0;
        for(int i = 0; i < errorMessages.size(); i++)
            if((tempseverity = ((FormError)errorMessages.elementAt(i)).getSeverity()) < severity)
                severity = tempseverity;

        return severity;
    }

    private Vector errorMessages;
    private HashMap errorFieldsMap;
}
