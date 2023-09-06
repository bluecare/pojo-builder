package io.github.muehmar.pojobuilder.snapshottesting;

import au.com.origin.snapshots.exceptions.SnapshotMatchException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

class IntellijDiffSnapshotTestExtension implements InvocationInterceptor {

  @Override
  public void interceptTestMethod(
      Invocation<Void> invocation,
      ReflectiveInvocationContext<Method> invocationContext,
      ExtensionContext extensionContext)
      throws Throwable {
    mapSnapshotMatchException(invocation);
  }

  @Override
  public void interceptTestTemplateMethod(
      Invocation<Void> invocation,
      ReflectiveInvocationContext<Method> invocationContext,
      ExtensionContext extensionContext)
      throws Throwable {
    mapSnapshotMatchException(invocation);
  }

  private static void mapSnapshotMatchException(Invocation<Void> invocation) throws Throwable {
    try {
      invocation.proceed();
    } catch (SnapshotMatchException e) {
      final List<Throwable> failures = e.getFailures();
      if (failures.size() == 1) {
        throw failures.get(0);
      } else {
        throw e;
      }
    }
  }
}
