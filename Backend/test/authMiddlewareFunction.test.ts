import { authMiddleware } from '../middlewares/authMiddleware.mjs';
import { verify } from 'jsonwebtoken';
import { NextFunction, Request, Response } from 'express';
jest.mock('jsonwebtoken', () => ({
  verify: jest.fn(),
}));
describe('authMiddleware', () => {
  let req: Request;
  let res: Response;
  let next: NextFunction;
  beforeEach(() => {
    req = {
      header: jest.fn(),
    } as unknown as Request;
    res = {
      status: jest.fn(),
      json: jest.fn(),
    } as unknown as Response;
    next = jest.fn();
  });
  it('should return 401 if no token is provided', () => {
    req.header.mockReturnValue(undefined);
    authMiddleware(req, res, next);
    expect(res.status).toHaveBeenCalledTimes(1);
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.json).toHaveBeenCalledTimes(1);
    expect(res.json).toHaveBeenCalledWith({ msg: 'No autorizado' });
  });
  it('should return 401 if token is invalid', () => {
    req.header.mockReturnValue('Bearer invalid-token');
    verify.mockImplementation(() => {
      throw new Error('Invalid token');
    });
    authMiddleware(req, res, next);
    expect(res.status).toHaveBeenCalledTimes(1);
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.json).toHaveBeenCalledTimes(1);
    expect(res.json).toHaveBeenCalledWith({ msg: 'Token inválido' });
  });
  it('should call next if token is valid', () => {
    req.header.mockReturnValue('Bearer valid-token');
    verify.mockImplementation(() => ({
      userId: '123',
    }));
    authMiddleware(req, res, next);
    expect(next).toHaveBeenCalledTimes(1);
    expect(req.user).toEqual({ userId: '123' });
  });
  it('should return 401 if error occurs during token verification', () => {
    req.header.mockReturnValue('Bearer valid-token');
    verify.mockImplementation(() => {
      throw new Error('Error verifying token');
    });
    authMiddleware(req, res, next);
    expect(res.status).toHaveBeenCalledTimes(1);
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.json).toHaveBeenCalledTimes(1);
    expect(res.json).toHaveBeenCalledWith({ msg: 'Token inválido' });
  });
});