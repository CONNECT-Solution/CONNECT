/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties;


/**
 *
 * @author A22387
 */
public class PropertiesException extends Exception
{
    /**
     * Default Constructor
     */
    public PropertiesException()
    {
        super();
    }

    /**
     * Create an excetption with a message
     *
     * @param sMessage A message about the exception
     */
    public PropertiesException(String sMessage)
    {
        super(sMessage);
    }

    /**
     * Create an exception with a message and the root cause
     *
     * @param sMessage A message about the exception
     * @param oRootCause The root cause of the exception
     */
    public PropertiesException(String sMessage, Throwable oRootCause)
    {
        super(sMessage, oRootCause);
    }

    /**
     * Create an exception with the root cause
     *
     * @param oRootCause The root cause of the exception
     */
    public PropertiesException(Throwable oRootCause)
    {
        super(oRootCause);
    }
}
