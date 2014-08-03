package com.mhacks.android.data;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
public class UnauthorizedException extends RuntimeException {
  public static final String UNAUTHORIZED = "User was unauthorized to perform this action.";

  public UnauthorizedException() { super(UNAUTHORIZED); }
}