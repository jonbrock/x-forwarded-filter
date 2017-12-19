/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Assertion utility class that assists in validating arguments.
 * <p>
 * <p>Useful for identifying programmer errors early and clearly at runtime.
 * <p>
 * <p>For example, if the contract of a public method states it does not
 * allow {@code null} arguments, {@code AssertM} can be used to validate that
 * contract. Doing this clearly indicates a contract violation when it
 * occurs and protects the class's invariants.
 * <p>
 * <p>Typically used to validate method arguments rather than configuration
 * properties, to check for cases that are usually programmer errors rather
 * than configuration errors. In contrast to configuration initialization
 * code, there is usually no point in falling back to defaults in such methods.
 * <p>
 * <p>This class is similar to JUnit's assertion library. If an argument value is
 * deemed invalid, an {@link IllegalArgumentException} is thrown (typically).
 * For example:
 * <p>
 * <pre class="code">
 * AssertM.notNull(clazz, "The class must not be null");
 * AssertM.isTrue(i > 0, "The value must be greater than zero");</pre>
 * <p>
 * <p>Mainly for internal use within the framework; consider
 * <a href="http://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a>
 * for a more comprehensive suite of {@code String} utilities.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Colin Sampaleanu
 * @author Rob Harrop
 * @since 1.1.2
 */
public class AssertM {

	private AssertM() {
		//utility class
	}

	/**
	 * AssertM a boolean expression, throwing an {@code IllegalStateException}
	 * if the expression evaluates to {@code false}.
	 * <p>Call {@link #isTrue} if you wish to throw an {@code IllegalArgumentException}
	 * on an assertion failure.
	 * <pre class="code">AssertM.state(id == null, "The id property must not already be initialized");</pre>
	 *
	 * @param expression a boolean expression
	 * @param message    the exception message to use if the assertion fails
	 * @throws IllegalStateException if {@code expression} is {@code false}
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}


	/**
	 * AssertM a boolean expression, throwing an {@code IllegalArgumentException}
	 * if the expression evaluates to {@code false}.
	 * <pre class="code">AssertM.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 *
	 * @param expression a boolean expression
	 * @param message    the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if {@code expression} is {@code false}
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that an object is {@code null}.
	 * <pre class="code">AssertM.isNull(value, "The value must be null");</pre>
	 *
	 * @param object  the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(/*@Nullable*/ Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that an object is not {@code null}.
	 * <pre class="code">AssertM.notNull(clazz, "The class must not be null");</pre>
	 *
	 * @param object  the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void notNull(/*@Nullable*/ Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="code">AssertM.hasLength(name, "Name must not be empty");</pre>
	 *
	 * @param text    the String to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the text is empty
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(/*@Nullable*/ String text, String message) {
		if (!StringUtils.hasLength(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * AssertM that the given String contains valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="code">AssertM.hasText(name, "'name' must not be empty");</pre>
	 *
	 * @param text    the String to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the text does not contain valid text content
	 * @see StringUtils#hasText
	 */
	public static void hasText(/*@Nullable*/ String text, String message) {
		if (!StringUtils.hasText(text)) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that the given text does not contain the given substring.
	 * <pre class="code">
	 * AssertM.doesNotContain(name, forbidden, () -&gt; "Name must not contain '" + forbidden + "'");
	 * </pre>
	 *
	 * @param textToSearch    the text to search
	 * @param substring       the substring to find within the text
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the text contains the substring
	 * @since 5.0
	 */
	public static void doesNotContain(/*@Nullable*/ String textToSearch, String substring, Supplier<String> messageSupplier) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}


	/**
	 * AssertM that an array contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="code">AssertM.notEmpty(array, "The array must contain elements");</pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array is {@code null} or contains no elements
	 */
	public static void notEmpty(/*@Nullable*/ Object[] array, String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that an array contains no {@code null} elements.
	 * <p>Note: Does not complain if the array is empty!
	 * <pre class="code">AssertM.noNullElements(array, "The array must contain non-null elements");</pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array contains a {@code null} element
	 */
	public static void noNullElements(/*@Nullable*/ Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}


	/**
	 * AssertM that a collection contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="code">AssertM.notEmpty(collection, "Collection must contain elements");</pre>
	 *
	 * @param collection the collection to check
	 * @param message    the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the collection is {@code null} or
	 *                                  contains no elements
	 */
	public static void notEmpty(/*@Nullable*/ Collection<?> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
	}


	/**
	 * AssertM that a Map contains entries; that is, it must not be {@code null}
	 * and must contain at least one entry.
	 * <pre class="code">AssertM.notEmpty(map, "Map must contain entries");</pre>
	 *
	 * @param map     the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the map is {@code null} or contains no entries
	 */
	public static void notEmpty(/*@Nullable*/ Map<?, ?> map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
	}


	/*@Nullable*/
	private static String nullSafeGet(/*@Nullable*/ Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

}
