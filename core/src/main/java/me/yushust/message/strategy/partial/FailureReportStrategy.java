package me.yushust.message.strategy.partial;

import me.yushust.message.strategy.Notify;

import java.util.List;

public interface FailureReportStrategy {

  void onFail(Notify.Failure cause, String text);

  void warn(Notify.Warning cause, List<String> pathStack);

}
