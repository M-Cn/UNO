package Main;

public class UpdateTickCtrl {
    public void UpdateDeltaTime(long _now) {
        DeltaTime = (LastFrameTime < 0) ? 0.0f : (_now - LastFrameTime) * 1E-9f;
        Fps = 1.f / DeltaTime;
        LastFrameTime = _now;
    }

    public float GetDeltaTime() { return DeltaTime; }
    public float GetFps() { return Fps; }

    private long LastFrameTime;
    private float DeltaTime;
    private float Fps;
}
