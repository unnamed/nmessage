package me.yushust.message;

import java.util.List;

public interface FailureListener {

  void onFail(Notify.Failure cause, String text);

  void warn(Notify.Warning cause, List<String> pathStack);

  FailureListener DUMMY = new FailureListener() {
    @Override
    public void onFail(Notify.Failure cause, String text) {
    }

    @Override
    public void warn(Notify.Warning cause, List<String> pathStack) {
    }
  };

}
