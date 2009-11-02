/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xtest.framework;

public class InstructionContext extends BasicContext {
	BasicTransaction transaction = null;
	
	public String getName() {
		return get("instruction_name");
	}

	public void setName(String name) {
		set("instruction_name", name);
	}

	public String error(String msg) {
		return "[Step " + get("step_id") + " Instruction " + get("instruction_name") + "]" + msg;
	}
	
//	public String getExpectedErrorMessage() {
//		return step().getExpectedErrorMessage();
//	}
//
//	public void setExpectedErrorMessage(String expectedErrorMessage) {
//		step().setExpectedErrorMessage(expectedErrorMessage);
//	}

	public InstructionContext(StepContext step) {
		super(step);   // parent_context
	}
	
	StepContext step() { return (StepContext) parent_context; }


}
