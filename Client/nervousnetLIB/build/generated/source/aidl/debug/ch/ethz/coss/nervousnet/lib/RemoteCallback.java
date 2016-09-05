/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/ales/Desktop/GIT/Aggregation-Proxy/Client/nervousnetLIB/src/main/aidl/ch/ethz/coss/nervousnet/lib/RemoteCallback.aidl
 */
package ch.ethz.coss.nervousnet.lib;
public interface RemoteCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements ch.ethz.coss.nervousnet.lib.RemoteCallback
{
private static final java.lang.String DESCRIPTOR = "ch.ethz.coss.nervousnet.lib.RemoteCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an ch.ethz.coss.nervousnet.lib.RemoteCallback interface,
 * generating a proxy if needed.
 */
public static ch.ethz.coss.nervousnet.lib.RemoteCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof ch.ethz.coss.nervousnet.lib.RemoteCallback))) {
return ((ch.ethz.coss.nervousnet.lib.RemoteCallback)iin);
}
return new ch.ethz.coss.nervousnet.lib.RemoteCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_success:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<ch.ethz.coss.nervousnet.lib.SensorReading> _arg0;
_arg0 = data.createTypedArrayList(ch.ethz.coss.nervousnet.lib.SensorReading.CREATOR);
this.success(_arg0);
reply.writeNoException();
reply.writeTypedList(_arg0);
return true;
}
case TRANSACTION_failure:
{
data.enforceInterface(DESCRIPTOR);
ch.ethz.coss.nervousnet.lib.ErrorReading _arg0;
_arg0 = new ch.ethz.coss.nervousnet.lib.ErrorReading();
this.failure(_arg0);
reply.writeNoException();
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements ch.ethz.coss.nervousnet.lib.RemoteCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void success(java.util.List<ch.ethz.coss.nervousnet.lib.SensorReading> list) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(list);
mRemote.transact(Stub.TRANSACTION_success, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(list, ch.ethz.coss.nervousnet.lib.SensorReading.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void failure(ch.ethz.coss.nervousnet.lib.ErrorReading reading) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_failure, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
reading.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_success = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_failure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void success(java.util.List<ch.ethz.coss.nervousnet.lib.SensorReading> list) throws android.os.RemoteException;
public void failure(ch.ethz.coss.nervousnet.lib.ErrorReading reading) throws android.os.RemoteException;
}
