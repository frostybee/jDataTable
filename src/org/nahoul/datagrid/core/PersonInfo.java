/*

Copyright (c) 2013 by Sleiman Rabah

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

package org.nahoul.datagrid.core;

public class PersonInfo {

    private String personID;
    private boolean isProcessed;
    private boolean isAccepted;
    private String email;
    private String firstName;
    private String lastName;

    public PersonInfo(String personID, String firstName, String lastName, String email,boolean isProcessed, boolean isAccepted ) {
        this.personID = personID;
        this.isProcessed = isProcessed;
        this.isAccepted = isAccepted;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    
    /**
     * Get the value of personID
     *
     * @return the value of personID
     */
    public String getRequestID() {
        return personID;
    }

    /**
     * Set the value of personID
     *
     * @param requestID new value of personID
     */
    public void setRequestID(String requestID) {
        this.personID = requestID;
    }

    public PersonInfo() {
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getDescription() {
        return email;
    }

    public void setDescription(String description) {
        this.email = description;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
 
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }


}
