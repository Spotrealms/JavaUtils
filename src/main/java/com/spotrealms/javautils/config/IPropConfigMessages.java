/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2019  Spotrealms Network
 *
 *    This library is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General License as
 *    published by the Free Software Foundation, either version 3 of the 
 *    License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.spotrealms.javautils.config;

//Package-private (all methods in this interface MUST have no modifiers to avoid exposing these to the dependencies besides those that implement this class)
public interface IPropConfigMessages {
	void warnLoadIntConf();
	void errorResolvePath(Throwable errorCause);
	void notifyConfDirCreated(String pathCreated);
	void errorPathCreationFail(String pathCreated);
	void notifyConfCopy(String confName);
	void errorConfCopy(String confName, Throwable errorCause);
	void notifyUsingIntConf();
	void notifyConfLoaded(String confName);
	void notifyDefReplaceNull(String defKey, String defVal, String confName);
	void notifyDefReplaceMissing(String defKey, String confName);
	void warnUVBadTypeDefInstead(String confKey, String checkType);
	void errorUVBadType(String confKey, String checkType, String assumedType);
	void notifySaved(String confPath, String confName);
	void notifySaveMatch(String confPath, String confName);
	void errorLoadFailed(String confPath, String confName, Throwable errorCause);
	void errorSaveFailed(String confPath, String confName, Throwable errorCause);
}