package me.yushust.message.strategy;

import me.yushust.message.MessageHandler;
import me.yushust.message.strategy.partial.FailureReportStrategy;
import me.yushust.message.strategy.partial.IncompatibleEntityStrategy;
import me.yushust.message.strategy.partial.MessageNotFoundStrategy;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

public class Strategy
    implements MessageNotFoundStrategy, FailureReportStrategy, IncompatibleEntityStrategy {

  private MessageNotFoundStrategy messageNotFoundStrategy = MessageNotFoundStrategy.RETURN_PATH;
  private FailureReportStrategy failureReportStrategy;
  private IncompatibleEntityStrategy incompatibleEntityStrategy;

  public Strategy onMessageNotFound(MessageNotFoundStrategy messageNotFoundStrategy) {
    this.messageNotFoundStrategy = Validate.notNull(messageNotFoundStrategy);
    return this;
  }

  public Strategy onFailure(FailureReportStrategy failureReportStrategy) {
    this.failureReportStrategy = failureReportStrategy;
    return this;
  }

  public Strategy onEntityIncompatibility(IncompatibleEntityStrategy incompatibleEntityStrategy) {
    this.incompatibleEntityStrategy = Validate.notNull(incompatibleEntityStrategy);
    return this;
  }

  @Override
  public void onFail(Notify.Failure cause, String text) {
    if (failureReportStrategy != null) {
      failureReportStrategy.onFail(cause, text);
    }
  }

  @Override
  public void warn(Notify.Warning cause, List<String> pathStack) {
    if (failureReportStrategy != null) {
      failureReportStrategy.warn(cause, pathStack);
    }
  }

  @Override
  public @Nullable String getNotFoundMessage(String language, String path) {
    return messageNotFoundStrategy.getNotFoundMessage(language, path);
  }

  @Override
  public void incompatibleEntity(Object fakeEntity) {
    if (incompatibleEntityStrategy != null) {
      incompatibleEntityStrategy.incompatibleEntity(fakeEntity);
    } else {
      throw new IllegalArgumentException("Incompatible entity '" + fakeEntity + "'");
    }
  }

  @Override
  public void failSendMessage(Object fakeEntity, String message) {
    if (incompatibleEntityStrategy != null) {
      incompatibleEntityStrategy.failSendMessage(fakeEntity, message);
    } else {
      Logger.getGlobal().warning("[" + MessageHandler.LIBRARY_NAME + "] Failed to send message " +
          "to incompatible entity '" + fakeEntity + "'. Message: '" + message + "'");
    }
  }
}
