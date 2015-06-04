/*
 * Bluegigas Bluetooth Smart Demo Application
 * Contact: support@bluegiga.com.
 *
 * This is free software distributed under the terms of the MIT license reproduced below.
 *
 * Copyright (c) 2012, Bluegiga Technologies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <winsock2.h>
#include "cmd_def.h"
#include <time.h>
extern int returnCurrentRSSI();
volatile HANDLE serial_handle;
SOCKET sclient;
struct message{
	int gatewayID;
	int rssi;
	int transmissionRange;
	WORD second;
};
void output(uint8 len1, uint8* data1, uint16 len2, uint8* data2) {
	DWORD written;

	if (!WriteFile(serial_handle, data1, len1, &written, NULL )) {
		printf("ERROR: Writing data. %d\n", (int) GetLastError());
		exit(-1);
	}

	if (!WriteFile(serial_handle, data2, len2, &written, NULL )) {
		printf("ERROR: Writing data. %d\n", (int) GetLastError());
		exit(-1);
	}
}
int read_message() {
	DWORD rread;
	const struct ble_msg *apimsg;
	struct ble_header apihdr;
	unsigned char data[256]; //enough for BLE

	/* read header */
	if (!ReadFile(serial_handle, (unsigned char*) &apihdr, 4, &rread, NULL )) {
		return GetLastError();
	}
	if (!rread)
		return 0;

	/* read rest if needed */
	if (apihdr.lolen) {
		if (!ReadFile(serial_handle, data, apihdr.lolen, &rread, NULL )) {
			return GetLastError();
		}
	}
	apimsg = ble_get_msg_hdr(apihdr);
	if (!apimsg) {
		printf("ERROR: Message not found:%d:%d\n", (int) apihdr.cls,
				(int) apihdr.command);
		return -1;
	}
	apimsg->handler(data);

	return 0;
}

void print_help() {
	printf("\tUsage: bluegiga_scan_example\tCOM-port\n");
}
DWORD WINAPI SendRSSI(LPVOID Param){
	int rssi = 0;
	struct message msg;
	msg.gatewayID = 3;
	while(1){
		if (read_message())
			{
				printf("Error reading message\n");
				break;
			}


			rssi = returnCurrentRSSI();
			printf("rssi value: %d\n",rssi);
			msg.rssi = rssi;
			msg.transmissionRange = 15;
			//printf("sending");
			sendMsg(msg);
			//printf("end sending");
	}

	return 0;
}

void sendMsg(struct message msg){

		char sendData[128];

		int count=0;
		int gatewayID = msg.gatewayID;
		int rssi = msg.rssi;
		int transmissionRange = msg.transmissionRange;
		SYSTEMTIME time;
		GetSystemTime(&time);
		msg.second = time.wSecond;

		//printf("rssi:%d",rssi);
		memcpy(sendData+count,&gatewayID,sizeof(gatewayID));

		count += sizeof(gatewayID);
		memcpy(sendData+count,&rssi,sizeof(rssi));
		count += sizeof(rssi);
		memcpy(sendData+count,&transmissionRange,sizeof(transmissionRange));
		count += sizeof(transmissionRange);
		memcpy(sendData+count,&msg.second,sizeof(msg.second));
		count += sizeof(msg.second);
		printf("count:%d\n",count);

		int a = send(sclient, sendData, count, 0);
		if (a == -1){
			perror("Error");
			return;
		}
		else{
			printf("message sended to the server\n");
		}

}

int main(int argc, char *argv[]) {
	/*
		 * create socket client
		 *
		 */
	WORD sockVersion = MAKEWORD(2,2);
		WSADATA data;
		if(WSAStartup(sockVersion, &data) != 0)
		{
			return -1;
		}
		if(!sclient){
			sclient = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
					if(sclient == INVALID_SOCKET)
					{
						printf("invalid socket !");
						return -1;
					}

					struct sockaddr_in serAddr;
					serAddr.sin_family = AF_INET;
					serAddr.sin_port = htons(5258);
					serAddr.sin_addr.S_un.S_addr = inet_addr("127.0.0.1");
					if (connect(sclient, (struct sockaddr *)&serAddr, sizeof(serAddr)) == SOCKET_ERROR)
					{
						printf("connect error !");
						closesocket(sclient);
						return -1;
					}
		}

	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);

	char str[80];


	if (argc < 2)
	{
		print_help();
		exit(-1);
	}
	snprintf(str, sizeof(str) - 1, "\\\\.\\%s", argv[1]);
	serial_handle = CreateFileA(str, GENERIC_READ | GENERIC_WRITE,
			FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL );

	if (serial_handle == INVALID_HANDLE_VALUE)
	{
		printf("Error opening serialport %s. %d\n", argv[1],
				(int) GetLastError());
		return -1;
	}

	DCB serialSettings;
	if (GetCommState(serial_handle, &serialSettings))
	{
		printf("Serial settings:\n----------------\n");
		printf("BaudRate:%ld\n", serialSettings.BaudRate = CBR_57600);
		printf("ByteSize:%d\n", serialSettings.ByteSize = 8);
		printf("Parity:%d\n", serialSettings.Parity = NOPARITY);
		printf("StopBits:%d\n", serialSettings.StopBits = ONESTOPBIT);
		printf("fOutxCtsFlow:%d\n", serialSettings.fOutxCtsFlow = TRUE);
		printf("fRtsControl:%d\n", serialSettings.fRtsControl = RTS_CONTROL_ENABLE);

		SetCommState(serial_handle, &serialSettings);
	}

	bglib_output = output;

	/* stop previous operation */
	//printf();
	//ble_cmd_system_hello();

	printf("[>] ble_cmd_gap_end_procedure()\n");
	ble_cmd_gap_end_procedure();
	printf("[>] ble_cmd_connection_get_status(0)\n");
	ble_cmd_connection_get_status(0);
	//ble_cmd_gap_set_scan_parameters(500000);

	printf("--------------------------------------------\n\n");
	printf("waiting for server command!\n");
	DWORD ThreadId;
	HANDLE ThreadHandle;

	/* Message loop */

	while (1)
	{
		char recData[255];
		int ret = recv(sclient, recData, 255, 0);
		if(ret > 0)
		{
			if(recData[0] == '1'){
				ThreadHandle = CreateThread(NULL,0,SendRSSI,NULL,0,&ThreadId);
				if(ThreadHandle != NULL){
					printf("create thread successful\n");
				}
				else{
					printf("error create thread \n");
				}
			}
			else if(recData[0] == '0'){
				CloseHandle(ThreadHandle);
			}
			else{
				printf("No command found!\n");
			}
		}

	}

	return 0;
}
