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


public class FormError
{

    public FormError(String errorMessage, int severity)
    {
        this.errorMessage = null;
        formField = null;
        this.severity = -1;
        display = false;
        displayColor = "RED";
        this.errorMessage = errorMessage;
        this.severity = severity;
    }

    public FormError(String errorMessage, String formField, int severity)
    {
        this.errorMessage = null;
        this.formField = null;
        this.severity = -1;
        display = false;
        displayColor = "RED";
        this.errorMessage = errorMessage;
        this.formField = formField;
        this.severity = severity;
    }

    public FormError(String errorMessage, String formField, int severity, boolean display, String displayColor)
    {
        this(errorMessage, severity);
        this.formField = formField;
        this.display = display;
        this.displayColor = displayColor;
    }

    public FormError(String errorMessage, int severity, boolean display, String displayColor)
    {
        this(errorMessage, severity);
        this.display = display;
        this.displayColor = displayColor;
    }

    public void setDisplay(boolean b)
    {
        display = b;
    }

    public boolean getDisplay()
    {
        return display;
    }

    public void setDisplayColor(String c)
    {
        displayColor = c;
    }

    public String getDisplayColor()
    {
        return displayColor;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public int getSeverity()
    {
        return severity;
    }

    public void setFormField(String formField)
    {
        this.formField = formField;
    }

    public String getFormField()
    {
        return formField;
    }

    private String errorMessage;
    private String formField;
    private int severity;
    private boolean display;
    private String displayColor;
    public static final int SEVERITY_STOP = 1;
    public static final int SEVERITY_WARNING = 2;
    public static final int SEVERITY_INFO = 3;
    public static final int SEVERITY_REPOST_DATA = 4;
    public static final String RED = "RED";
    public static final String BLACK = "BLACK";

}
