import * as net from 'node:net';
//
export async function findAvailablePort(desirePort) {
    const server = net.createServer();
    return new Promise((resolve, reject) => {
        server.listen(desirePort, () => {
            const { port } = server.address();
            server.close(() => resolve(port));
        });
        server.on('error', (err) => {
            // If the port is in use, try to find the next available port.
            if (err.code === "EADDRINUSE") findAvailablePort(0).then(port => resolve(port));
            else reject(err);
        });
    });
}