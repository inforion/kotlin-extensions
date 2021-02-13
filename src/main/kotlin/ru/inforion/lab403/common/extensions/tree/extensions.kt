package ru.inforion.lab403.common.extensions.tree

import java.io.Serializable

fun <T: Serializable> Node<T>.filterChildren(predicate: (Node<T>) -> Boolean) = children.filter(predicate)

fun <T: Serializable> Node<T>.findChild(predicate: (Node<T>) -> Boolean) = children.find(predicate)

fun <T: Serializable> Node<T>.forEachChild(action: (Node<T>) -> Unit) = children.forEach(action)

fun <T: Serializable, R> Node<T>.mapChildren(transform: (Node<T>) -> R) = children.map(transform)