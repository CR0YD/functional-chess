package com.croyd.functional_chess;

import java.util.Optional;
import java.util.function.Function;

import com.croyd.functional_chess.TailCalls.TailCall;

public class Stack<T> {
	
	private final Optional<Element<T>> HEAD;

	public Stack() {
		this.HEAD = Optional.of(new Element<T>());
	}

	private Stack(final Element<T> head) {
		this.HEAD = Optional.of(head);
	}

	private Stack(final Optional<Element<T>> head) {
		this.HEAD = head;
	}

	public Stack<T> add(final T value) {
		return new Stack<T>(new Element<T>(value, this.HEAD));
	}

	public Stack<T> add(final T value, final boolean unique) {
		if (unique && !this.contains(value))
			return this.add(value);

		return this;
	}

	public Stack<T> add(Stack<T> stack) {
		return this.mergeStacks(this.invertStack(stack, new Stack<T>()).invoke(), this).invoke();
	}

	public Stack<T> add(Stack<T> stack, final boolean unique) {
		if (unique)
			return this.add(this.filterDuplicateElements(stack.invert(), this, new Stack<T>()).invoke());

		return this.add(stack);
	}

	private TailCall<Stack<T>> filterDuplicateElements(final Stack<T> stack, final Stack<T> base,
			final Stack<T> result) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		if (base.contains(stack.first().get()))
			return TailCalls.call(filterDuplicateElements(stack.remove(), base, result));

		return TailCalls.call(filterDuplicateElements(stack.remove(), base, result.add(stack.first().get())));
	}

	private TailCall<Stack<T>> mergeStacks(final Stack<T> stack, final Stack<T> result) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		return TailCalls.call(this.mergeStacks(stack.remove(), result.add(stack.first().get())));
	}

	public Stack<T> invert() {
		return invertStack(this, new Stack<T>()).invoke();
	}

	private TailCall<Stack<T>> invertStack(final Stack<T> stack, final Stack<T> result) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		return TailCalls.call(this.invertStack(stack.remove(), result.add(stack.first().get())));
	}

	private <K> Stack<T> filter(final Function<T, K> filter, final K value) {
		return filterStack(this.invert(), filter, value, new Stack<T>()).invoke();
	}

	private <K> TailCall<Stack<T>> filterStack(
		final Stack<T> stack,
		final Function<T, K> filter,
		final K value,
		final Stack<T> result
	) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		if (value.equals(filter.apply(stack.first().get())))
			return TailCalls.call(filterStack(stack.remove(), filter, value, result.add(stack.first().get())));

		return TailCalls.call(filterStack(stack.remove(), filter, value, result));
	}

	public <K> Stack<T> filter(final Function<T, K> filter, final Stack<K> values) {
		return filterStack(filter, values, new Stack<T>()).invoke();
	}

	private <K> TailCall<Stack<T>> filterStack(
		final Function<T, K> filter,
		final Stack<K> values,
		final Stack<T> result
	) {
		if (values.isEmpty())
			return TailCalls.done(result);

		return TailCalls.call(
			filterStack(
				filter,
				values.remove(),
				result.add(this.invert().filter(filter, values.first().get()), true)
			)
		);
	}
	
	private <K> Stack<T> filter(final Function<T, K> filter, final K value, final boolean equals) {
		return filterStack(this.invert(), filter, value, equals, new Stack<T>()).invoke();
	}

	private <K> TailCall<Stack<T>> filterStack(
		final Stack<T> stack,
		final Function<T, K> filter,
		final K value,
		final boolean equals,
		final Stack<T> result
	) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		if (equals && value.equals(filter.apply(stack.first().get())))
			return TailCalls.call(filterStack(stack.remove(), filter, value, equals, result.add(stack.first().get())));

		if (!equals && !value.equals(filter.apply(stack.first().get())))
			return TailCalls.call(filterStack(stack.remove(), filter, value, equals, result.add(stack.first().get())));

		return TailCalls.call(filterStack(stack.remove(), filter, value, equals, result));
	}
	
	public <K> Stack<T> filter(final Function<T, K> filter, final Stack<K> values, final boolean equals) {
		return filterStack(filter, values, equals, new Stack<T>()).invoke();
	}

	private <K> TailCall<Stack<T>> filterStack(
		final Function<T, K> filter,
		final Stack<K> values,
		final boolean equals,
		final Stack<T> result
	) {
		if (values.isEmpty())
			return TailCalls.done(result);

		return TailCalls.call(
			filterStack(
				filter,
				values.remove(),
				equals,
				result.add(this.invert().filter(filter, values.first().get(), equals), true)
			)
		);
	}
	
	public Stack<T> replace(final T oldValue, final T newValue) {
		return replaceInStack(this.invert(), oldValue, newValue, new Stack<T>()).invoke();
	}
	
	private TailCall<Stack<T>> replaceInStack(final Stack<T> stack, final T oldValue, final T newValue, final Stack<T> result) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		if (stack.first().get().equals(oldValue))
			return TailCalls.call(replaceInStack(stack.remove(), oldValue, newValue, result.add(newValue)));

		return TailCalls.call(replaceInStack(stack.remove(), oldValue, newValue, result.add(stack.first().get())));
	}
	
	public <K> Stack<K> map(final Function<T, K> function) {
		return mapCall(this.invert(), function, new Stack<K>()).invoke();
	}
	
	private <K> TailCall<Stack<K>> mapCall(final Stack<T> stack, final Function<T, K> function, final Stack<K> result) {
		if (stack.isEmpty())
			return TailCalls.done(result);
		
		return TailCalls.call(mapCall(stack.remove(), function, result.add(function.apply(stack.first().get()))));
	}

	public boolean contains(final T value) {
		return stackContains(this, value).invoke();
	}

	private TailCall<Boolean> stackContains(final Stack<T> stack, final T value) {
		if (stack.isEmpty())
			return TailCalls.done(false);

		if (stack.first().get().equals(value))
			return TailCalls.done(true);

		return TailCalls.call(stackContains(stack.remove(), value));
	}

	public Stack<T> remove() {
		return new Stack<T>(this.HEAD.get().getNext());
	}

	public Optional<T> first() {
		return this.HEAD.get().VALUE;
	}

	public boolean isEmpty() {
		return !this.HEAD.get().VALUE.isPresent();
	}

	public String toString() {
		return stringifyStack(this, new String()).invoke();
	}
	
	private TailCall<String> stringifyStack(final Stack<T> stack, final String result) {
		if (stack.isEmpty())
			return TailCalls.done(result);

		if (result.isEmpty())
			return TailCalls.call(stringifyStack(stack.remove(), stack.first().get().toString()));

		return TailCalls.call(stringifyStack(stack.remove(), result + " -> " + stack.first().get().toString()));
	}
	
	private class Element<T_E> {

		public final Optional<T_E> VALUE;
		private final Optional<Element<T_E>> NEXT;

		public Element() {
			this.VALUE = Optional.empty();
			this.NEXT = Optional.empty();
		}

		public Element(final T_E value, final Optional<Element<T_E>> next) {
			this.VALUE = Optional.of(value);
			this.NEXT = next;
		}

		public Optional<Element<T_E>> getNext() {
			return Optional.of(this.NEXT.orElse(new Element<T_E>()));
		}
	}
}
